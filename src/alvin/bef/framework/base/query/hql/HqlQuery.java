package alvin.bef.framework.base.query.hql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import alvin.bef.framework.base.exception.ApiError;
import alvin.bef.framework.base.exception.ApiException;
import alvin.bef.framework.base.exception.ValidateException;
import alvin.bef.framework.base.model.Entity;
import alvin.bef.framework.base.model.SoftDelete;
import alvin.bef.framework.base.session.manager.SessionManager;

/**
 * object to parse the query string into objects, fields and filters. builds
 * a HQL string (and HQL parameters) that can be used directly with HQL
 * 
 * @author Alvin
 */
public class HqlQuery {

   public static final Pattern PATTERN_QUERY = Pattern
   .compile(
      "^\\s*select\\s+(\\w+(?:\\s*,\\s*\\w+)*)\\s+from\\s+(\\w+)\\s*(\\s+where\\s+\\w+\\s*(?:>=|<=|>|<|!=|=|like)\\s*(?:\\S+|'.*')(?:\\s+(and|or)\\s+\\w+\\s*(?:>=|<=|>|<|!=|=|like)\\s*(?:\\S+|'.*'))*+)?\\s*(?:LIMIT \\w+)*$",
      Pattern.CASE_INSENSITIVE);

   private final SessionManager sessionManager;
   private HQLJoinRefBuilder joinRefBuilder;
   
   private final String zoql;
   private final String zObject;
   //private final ApiField[] fields;
   //private final ApiClassMapper classMapper;

   private final String hqlFields;
   private final String hqlFrom;
   private final String hqlFromOnlyJoinWhereFields; // We are no need to join tables if the reference fields are in select only for loading id
   private final String hqlWhere;

   private boolean specialQueryReturn;
   private Object[] specialQueryResult;
   private String specialQueryString;
   private Object[] specialQueryParam;

   private final String filter;
   private List<Object> params;
   private boolean querySpecials = false;
   private Object[] whereDetail = new Object[3];
   private int index = -1;
   //private final Map<String,ApiField> joinFields = new HashMap<String,ApiField>();
   private boolean preservSelectColumnOrder = false;
   private boolean includeDeleletdRows = false;
   private boolean includeDeletedColumn = false;
   public boolean isFieldInWhere(String apiName) {
      for (int i = 0; i < whereDetail.length; i++) {
         Object[] opers = (Object[]) whereDetail[i];
         if (opers == null) break;
         //ApiField f = (ApiField) opers[0];
         //if (f.getMapping().getApiName().equalsIgnoreCase(apiName)) return true;
      }
      return false;
   }

   private Object[] getWhereDetail() {
      if (index == -1) return new Object[0];
      whereDetail = ApiHelper.trimArray(whereDetail, index);
      return whereDetail;
   }

   private void initJoinSet(ApiField field, ApiClassMapper classMapper, Map<String,ApiField> joins) {
      String join = classMapper.getJoinStringFromNameToJoin(field);
      if (join != null) {
         joins.put(join,field);
      }
   }

   private Map<String,ApiField> initJoinSet(Object[] opers, ApiClassMapper classMapper, Map<String,ApiField> joins) {
      if (opers != null && opers.length > 0) {
         Map<String,ApiField> joinsInWhere = new HashMap<String,ApiField>();
         for (int i = 0; i < opers.length ; i++) {
            Object[] oper = (Object[]) opers[i];
            if (oper != null) {
               ApiField field = (ApiField) oper[0];
               initJoinSet(field, classMapper, joinsInWhere);
            }
         }
         if(joinsInWhere.size()>0){
            joins.putAll(joinsInWhere);
            return joinsInWhere;
         }
      }
      return Collections.emptyMap();
   }

   private void addToWhereDetail(Object o) {
      if (index == whereDetail.length - 1) {
         Object[] temp = new Object[whereDetail.length + 3];
         System.arraycopy(whereDetail, 0, temp, 0, whereDetail.length);
         whereDetail = temp;
      }
      ++index;
      whereDetail[index] = o;
   }
   private String selectPart = null,objectPart=null,wherePart=null;
   private void validateQueryString(String query) {
      if (query == null) throw new ValidateException(ApiError.MALFORMED_QUERY, "Query string can not be null.");
      query = query.trim();
//      String qsLower = query.toLowerCase();
      Matcher m = PATTERN_QUERY.matcher(query);
      if (!m.matches()) {
         throw new ValidateException(ApiError.MALFORMED_QUERY, "You have an error in your ZOQL syntax");
      }
      
//      if(m.find()){
      selectPart = m.group(1).trim().toLowerCase();
      objectPart = m.group(2).trim().toLowerCase();
      try{
         wherePart = m.group(3).trim();
      }catch(Exception e){
         wherePart = null;
      }
//      }
   }

   public HqlQuery(SessionManager sessionManager, ApiClassMapperFactory apiMapper, String queryString,HQLJoinRefBuilder builder) throws AxisFault {
	   this(sessionManager, apiMapper, queryString, builder, false, false, false);
   }
   
   public HqlQuery(SessionManager sessionManager, ApiClassMapperFactory apiMapper, String queryString,HQLJoinRefBuilder builder, boolean preservSelectColumnOrder, boolean includeDeleletdRows, boolean includeDeletedColumn) throws AxisFault {
	  this.preservSelectColumnOrder = preservSelectColumnOrder;
	  this.includeDeleletdRows  = includeDeleletdRows;
	  this.includeDeletedColumn = includeDeletedColumn;
      this.sessionManager = sessionManager;
      this.joinRefBuilder = builder;
      
      this.zoql = queryString;
      validateQueryString(queryString);
      this.params = new ArrayList<Object>();

      if (queryString == null) {
         throw new ValidateException(ApiError.MALFORMED_QUERY, "no query specified");
      }

      // String qs = queryString.replaceAll("\\s{2,}", " ");
      this.zObject = getObject();
      this.classMapper = apiMapper.getApiClassMapperByApiNameForCurrentUser(zObject);
      if (this.getApiClassMapper() == null) {
         throw new ValidateException(ApiError.INVALID_TYPE, "invalid type specified: " + this.zObject);
      }

      if (!getApiClassMapper().getApiClassMapping().isAccessibleForOperation(ApiOperation.READ)) {
         throw new ApiException(ApiError.INVALID_TYPE, "invalid type for query");
      }

      this.filter = getFilter(queryString);

      String[] fieldNames = getFields(queryString);
      List<ApiField> fieldList = new ArrayList<ApiField>();
      // always select id
      List<String> hqlFields = new LinkedList<String>();
      if (this.includeDeletedColumn) {
    	  DummyApiField<Boolean> deleteFiled = new DummyApiField<Boolean>(Boolean.class, SoftDelete.FIELD_DELETED);
    	  fieldList.add(deleteFiled);
    	  hqlFields.add( ApiQuery.OBJECT_ALIAS+"."+SoftDelete.FIELD_DELETED);
      }
      
      if (!preservSelectColumnOrder) {
    	  hqlFields.add(ApiQuery.OBJECT_ALIAS+".id ");
    	  fieldList.add(getApiClassMapper().getField("id".toLowerCase()));
      }

      Set<String> handledFields = new HashSet<String>();
      for (int i = 0; i < fieldNames.length; i++) {
         String fieldName = fieldNames[i];
         if ("id".equals(fieldName) && !this.preservSelectColumnOrder) {
            continue;
         }
         else {
            ApiField field = getApiClassMapper().getField(fieldName.toLowerCase());
            if (field == null || !field.getMapping().isAccessibleForOperation(ApiOperation.READ) || field.getMapping().isSecure()) {
               throw new ValidateException(ApiError.INVALID_FIELD, "invalid field for query: " +
                  getApiClassMapper().getApiClassMapping().getApiName() + "." + (field != null ? field.getMapping().getApiName() : fieldName));
            }
            else {
               querySpecials = querySpecials == false ? field.isSpecQuery() : querySpecials;
               

               // in version 7.0 and above, we remove duplicates from query, and therefore in response.
               if (AxisContext.getContext().getVersion().getValue() < ApiVersion.VERSION_7_0.getValue()) {
            	  hqlFields.add(field.getHqlRef(ApiQuery.OBJECT_ALIAS));
                  fieldList.add(field);
               }
               else {
                  String apiName = field.getMapping().getApiName().toLowerCase();
                  if (!handledFields.contains(apiName)) {
                	 hqlFields.add(field.getHqlRef(ApiQuery.OBJECT_ALIAS));
                     fieldList.add(field);
                     handledFields.add(apiName);
                  }
               }
               initJoinSet(field, getApiClassMapper(), joinFields);
            }
         }
      }

      String[] fields = getApiClassMapper().getAdditionQueryFields();
      if (fields != null) for (String fil : fields)
    	  hqlFields.add(ApiQuery.OBJECT_ALIAS+"."+fil);

      String[] joinStr = getApiClassMapper().getAdditionJoinFields();
      if (joinStr != null){ 
    	  for (String fil : joinStr){
    		  joinFields.put(fil,new HardCodeJoinApiField(fil));
    	  }
      }

      this.fields = fieldList.toArray(new ApiField[fieldList.size()]);

      // standard stuff
      StringBuilder hqlWhere = new StringBuilder();
      ApiQuery.addStandardFilter(sessionManager.getUserSession(), hqlWhere, ApiQuery.OBJECT_ALIAS, getApiClassMapper(), this.includeDeleletdRows);
      addCustomFilter(hqlWhere, ApiQuery.OBJECT_ALIAS, getApiClassMapper());
      Map<String,ApiField> joinsInWhere = initJoinSet(whereDetail, getApiClassMapper(), joinFields);
      getApiClassMapper().addApiFilter(hqlWhere, ApiQuery.OBJECT_ALIAS, this);

      StringBuilder hqlFrom = new StringBuilder();
      hqlFrom.append(getApiClassMapper().getHqlName()).append(" ").append(ApiQuery.OBJECT_ALIAS).append(" ");
      ApiHelper.appendLeftJoins(hqlFrom, joinFields, ApiQuery.OBJECT_ALIAS);
      
      StringBuilder hqlFromOnlyForWhere = new StringBuilder();
      hqlFromOnlyForWhere.append(getApiClassMapper().getHqlName()).append(" ").append(ApiQuery.OBJECT_ALIAS).append(" ");
      appendLeftJoinsForFieldsInWhere(hqlFromOnlyForWhere, joinsInWhere, ApiQuery.OBJECT_ALIAS);
      
      this.hqlFromOnlyJoinWhereFields = hqlFromOnlyForWhere.toString();
      this.hqlFields = StringUtils.join(hqlFields, " , ");
      this.hqlFrom = hqlFrom.toString();
      this.hqlWhere = hqlWhere.toString();

//      if (ApiContext.getContext().getVersion().getValue() >= ApiVersion.VERSION_6_0.getValue()) {
//         this.hqlLimit = getLimit(queryString);
//      }
//      else {
//         this.hqlLimit = -1;
//      }

      handleSpecialQuery();
   }
   
   private void appendLeftJoinsForFieldsInWhere(StringBuilder hqlFromOnlyForWhere, Map<String,ApiField> joinsInWhere, String alias){
      hqlFromOnlyForWhere.append(joinRefBuilder.buildJoinRef(this, joinsInWhere,alias,sessionManager.getUserSession()));
   }
   
   private void handleSpecialQuery() {
      // BUGBUG: any reason this is outside HqlQuery object? it makes doing query cursors difficult...
      if (querySpecials) {
         SpecialApiFieldFactory iafca = (SpecialApiFieldFactory) getApiClassMapper().getCrud();
         Object[] hqlParams = iafca.handleQueryContainSpecFields(this.fields, getWhereDetail(), getApiClassMapper(), joinFields);
         if (hqlParams == null) {
            specialQueryReturn = true;
            specialQueryResult = null;
            return;
         }
         else if (hqlParams.length == 1) {
            // invoice body query want to query result and do some calcualte in manager and return result directly
            specialQueryReturn = true;
            specialQueryResult = (Object[]) hqlParams[0];
            return;
         }
         this.specialQueryString = (String) hqlParams[0];
         this.specialQueryParam = (Object[]) hqlParams[1];
         specialQueryString = specialQueryString.trim();
         StringBuilder sb = new StringBuilder(specialQueryString);
         Matcher m = ApiQuery.PATTERN_WHERE.matcher(specialQueryString);
         if (m.find()) sb.append(" and ");
         else sb.append(" where ");
         ApiQuery.addStandardFilter(sessionManager.getUserSession(), sb, ApiQuery.OBJECT_ALIAS, getApiClassMapper());
         
         /*
          * add additional filter missed in special query
          */
         if(AxisContext.getContext().getVersion().getValue()>=ApiVersion.VERSION_22_0.getValue())
            getApiClassMapper().addApiFilter(sb, ApiQuery.OBJECT_ALIAS, this);
         
         specialQueryString = sb.toString();
      }
   }

   public String getQuery() {
      if (this.specialQueryString != null) return this.specialQueryString;
      StringBuilder str = new StringBuilder("SELECT ");
      str.append(this.hqlFields);
      str.append(" FROM ");
      str.append(this.hqlFrom);
      if(this.hqlWhere!=null&&this.hqlWhere.length()>0){
    	  str.append(" WHERE ");
    	  str.append(this.hqlWhere);
      }
      return str.toString();
   }
   /**
    * 
    * @return
    */
   public String getWhere() {
	   return this.hqlWhere;
   }

   /*
    * the query for cursor does not include the original query's WHERE
    * clause. this is because the initial query did the filtering; all we
    * want are IDs.
    */
   public String getQueryForCursor() {
      if (this.specialQueryString != null) return getSpecialQueryStringForCursor(this.specialQueryString);
      StringBuilder str = new StringBuilder("SELECT ");
      str.append(this.hqlFields);
      str.append(" FROM ");
      str.append(this.hqlFrom);
      str.append(" WHERE ");
      return str.toString();
   }

   private String getSpecialQueryStringForCursor(String queryString) {
      String q = queryString.toLowerCase();
      if (q.indexOf(" where ") > 0) {
         return queryString.concat(" and ");
      }
      return queryString;
   }

   private String getQueryStringAfterFrom(String queryString) {
      String st = queryString.toLowerCase();
      if (!(st.indexOf(" from ") > 0)) {
         return queryString;
      }
      else {
         int i = st.indexOf(" from ");
         return st = queryString.substring(i + 6);
      }
   }

   public String getQueryIds() {
      StringBuilder str = new StringBuilder("SELECT ").append(ApiQuery.OBJECT_ALIAS).append(".id FROM ");
      if (this.specialQueryString != null) return str.append(getQueryStringAfterFrom(specialQueryString)).toString();
      str.append(this.hqlFromOnlyJoinWhereFields);
      if(this.hqlWhere!=null&&this.hqlWhere.length()>0){
    	  str.append(" WHERE ");
    	  str.append(this.hqlWhere);
      }
      return str.toString();
   }

   /**
    * add user-specified scope from query string
    * 
    * @param hql
    * @param alias
    * @param mapping2
    * @return
    * @throws AxisFault 
    * @
    */
   private List<Object> addCustomFilter(StringBuilder hql, String alias, ApiClassMapper classMapper) throws AxisFault  {

      if (this.filter != null) {
    	  if(hql!=null && hql.length()>0)
    		  hql.append(" AND (");
    	  else{
    		  hql.append(" (");
    	  }
    	  AtomicInteger counter = new AtomicInteger(0);
         handleFilter(hql, alias, this.filter,counter);
         hql.append(" )");
      }

      return getParams();

   }
   
   private Pattern getSingleQuotePatternByVersion(){
      if(ApiContext.getContext().getVersion().getValue() >= ApiVersion.VERSION_40_0.getValue()){
         return ApiQuery.SINGLE_QUOTES_SUPPORT_ESCAPE;
      }else{
         return ApiQuery.SINGLE_QUOTES;
      }
   }

   private String getValue(String filter) {
      String stringValue = null;
      filter = filter.trim();
      if (filter.startsWith("'")) {
         Matcher m = getSingleQuotePatternByVersion().matcher(filter);
         if (m.find()) {
            stringValue = m.group();
         }
         else {
            throw new ValidateException(ApiError.MALFORMED_QUERY, "You have an error in your ZOQL syntax; your query might be missing a single quote.");
         }
      }
      else {
         String[] pair = ApiQuery.PATTERN_ANDOR.split(filter);
         stringValue = pair[0];
      }
      if (stringValue == null) throw new ValidateException(ApiError.MALFORMED_QUERY, "You have an error in your ZOQL syntax");
      return stringValue;
   }

   public static int FILTER_COUNT_LIMIT=200;
   private void checkCountForFilter(AtomicInteger counter){
	   int count = counter.incrementAndGet();
	   if(count > FILTER_COUNT_LIMIT){
		   throw new ValidateException(ApiError.MALFORMED_QUERY, "You have exceeded the maximum number of parameters in the Where clause.");
	   }
   }
   
   private void handleFilter(StringBuilder hql, String alias, String filter, AtomicInteger counter) throws AxisFault  {
	  checkCountForFilter(counter);
      // get field being filtered
      Matcher matcher = ApiQuery.PATTERN_FIELD.matcher(filter);
      Object[] operDetail = new Object[4];
      String fieldName = ApiQuery.getToken(matcher);
      if(fieldName==null){
    	  throw new ValidateException(ApiError.MALFORMED_QUERY, "invalid filter: ".concat(filter));
      }
      ApiField field = getApiClassMapper().getField(fieldName.toLowerCase());
      
      operDetail[0] = field;
      querySpecials = querySpecials || field == null ? querySpecials : field.isSpecQuery();
      if (field == null) {
         throw new ValidateException(ApiError.MALFORMED_QUERY, "invalid field for filter: "
            + (field != null ? field.getMapping().getApiName() : fieldName));
      }

      // get operator
      filter = filter.substring(filter.indexOf(fieldName) + fieldName.length()).trim();
      matcher = ApiQuery.PATTERN_OPERATOR.matcher(filter);
      String op = ApiQuery.getToken(matcher);
      if (op == null) {
         throw new ValidateException(ApiError.MALFORMED_QUERY, "invalid comparator for query: " + filter);
      }
      operDetail[1] = op;
      filter = filter.substring(filter.indexOf(op) + op.length()).trim();

      // get value for filter AND next filter clause
      String stringValue = getValue(filter);
      filter = filter.substring(filter.indexOf(stringValue) + stringValue.length());
      Object value = field.getHqlFilterValue(stringValue);
      CaseSensitiveValueContainer vcForCaseSensitiveOptimizer = null;
      // can only do leading/tailing like
      if ("like".equals(op)) {
         if (!(value instanceof String)) {
            throw new ValidateException(ApiError.INVALID_VALUE, "invalid value for like: " + stringValue);
         }
         if (AxisContext.getContext().getVersion().getValue() >= ApiVersion.VERSION_6_0.getValue()) {
            String likeValue = (String) value;
            boolean startsWith = likeValue.startsWith("%");
            boolean endsWith = likeValue.endsWith("%");
            boolean contains = likeValue.length() > 2 && likeValue.substring(1, likeValue.length() - 1).contains("%");
            if ((startsWith && endsWith) || contains) {
               throw new ValidateException(ApiError.INVALID_VALUE, "only trailing or leading % allowed");
            }
         }
      }

      if ("null".equalsIgnoreCase(stringValue)) {
         hql.append(field.getHqlFilterName(alias));
         if ("=".equals(op)) {
            value = "IS NULL";
            hql.append(" IS NULL ");
         }
         else if ("!=".equals(op)) {
            value = "IS NOT NULL";
            hql.append(" IS NOT NULL ");
         }
         else {
            throw new ValidateException(ApiError.INVALID_VALUE, "invalid comparator for null: " + op);
         }
      }
      else if (value != null) {
         
         String filterField = null;
         
         /**
          * we need to move logic to getHqlFitlerValue(), but let's hold on and change in release 123, because this will impact more files.
          */
         if (ApiHelper.isApiCaseSensitiveField(field)){
        	    filterField = ((StringField)field).getHqlFilterNameForCaseSensitive(alias, op);
        	    getParams().add(value);
        	    vcForCaseSensitiveOptimizer = new CaseSensitiveValueContainer((String) value,((String)value).getBytes());
        	    value = ((String)value).getBytes();
         }
         
         if(filterField==null){
            filterField = field.getHqlFilterName(alias);
         }
         
         appendHqlQueryField(hql, field, op, value, filterField);
      }
      else {
         throw new ValidateException(ApiError.INVALID_VALUE, "invalid value for query: " + stringValue);
      }
      operDetail[2] = vcForCaseSensitiveOptimizer == null?value:vcForCaseSensitiveOptimizer;

      // if another filter clause exists, recursively add it
      addToWhereDetail(operDetail);
      if (filter.length() > 0) {
         matcher = ApiQuery.PATTERN_ANDOR.matcher(filter);
         String t = ApiQuery.getToken(matcher);
         if (t == null) throw new ValidateException(ApiError.MALFORMED_QUERY, "You have an error in your ZOQL syntax");
         hql.append(" ").append(t).append(" ");
         filter = filter.substring(filter.indexOf(t) + t.length()).trim();
         operDetail[3] = t;
         handleFilter(hql, alias, filter,counter);
      }
   }

   private void appendHqlQueryField(StringBuilder hql, ApiField field,
		   String op, Object value, String filterField) {
	   // deal with the 'date' and operator
	   boolean needSpeciallyHandleDateInput = (ApiFieldType.DateType
			   .equals(field.getApiFieldType()) || ApiFieldType.ZDateType
			   .equals(field.getApiFieldType())) && (value instanceof java.util.Date)
			   && TzUtils.isNewApiForTz() ;
	   if (needSpeciallyHandleDateInput) {
		   // translate date > 2014-01-01 ==> date >= 2014-01-02 ,
		   if (">".equals(op)) {
			   hql.append(filterField);
			   hql.append(" >= ");
			   hql.append("?");
			   getParams().add(getNextDate((java.util.Date) value));
		   }
		   // date <= 2014-01-01 ==> date < 2014-01-02
		   else if ("<=".equals(op)) {
			   hql.append(filterField);
			   hql.append(" < ");
			   hql.append("?");
			   getParams().add(getNextDate((java.util.Date) value));
		   }
		   // date=2014-01-01 ==> (date>=2014-01-01 AND
		   // date<2014-01-02)
		   else if ("=".equals(op)) {
			   // original value as start
			   hql.append(filterField);
			   hql.append(" >= ");
			   hql.append("?");
			   getParams().add(value);
			   
			   hql.append(" AND ");
			   hql.append(filterField);
			   hql.append(" < ");
			   hql.append("?");
			   getParams().add(getNextDate((java.util.Date) value));
		   } else {
			   hql.append(filterField);
			   hql.append(" ").append(op).append(" ");
			   hql.append("?");
			   getParams().add(value);
		   }
	   } else {
		   hql.append(filterField);
		   hql.append(" ").append(op).append(" ");
		   hql.append("?");
		   getParams().add(value);
	   }
   }

   private java.sql.Timestamp getNextDate(java.util.Date value) {
	   Calendar cal = Calendar.getInstance();
	   cal.setTime(value);
	   cal.add(Calendar.DATE, 1);
	   return new java.sql.Timestamp(cal.getTimeInMillis());
   }

   private String getFilter(String queryString)  {
      queryString = this.wherePart;
      if(queryString==null || queryString.length()<=0){
         return null;
      }
      queryString = this.wherePart;
      String filter = getPart(queryString, "where", getFilterBetweenLimit(queryString));
      if (filter == null) {
         filter = getPart(queryString, "where", null);
      }
      if (queryString.indexOf(" where ") != -1 && filter == null) {
         throw new ValidateException(ApiError.MALFORMED_QUERY, "no filter criteria specified");
      }
      return filter;
   }
   
   private String getFilterBetweenLimit(String queryString){
	   Matcher matcher = ApiQuery.PATTERN_LIMIT.matcher(queryString);
       String t = ApiQuery.getToken(matcher);
       return t;
   }

//   private int getLimit(String queryString)  {
//      int limit = -1;
//      boolean limitSpecified = queryString.indexOf(" limit ") != -1;
//      if (limitSpecified) {
//         String limitString = getPart(queryString, "limit", null);
//         if (limitString == null) {
//            throw new ValidateException(ApiError.MALFORMED_QUERY, "no limit specified");
//         }
//         else {
//            try {
//               limit = Integer.valueOf(limit);
//            }
//            catch (NumberFormatException e) {
//               throw new ValidateException(ApiError.MALFORMED_QUERY, "invalid limit: " + limit);
//            }
//         }
//      }
//
//      return limit;
//   }

   private String getObject()  {
      String object = this.objectPart;
      if (object == null) {
         throw new ValidateException(ApiError.MALFORMED_QUERY, "no object specified");
      }
      object = StringUtils.chomp(object);
      return object;
   }

   private String[] getFields(String queryString)  {
      String selectFields = this.selectPart;
      if (selectFields == null || selectFields.trim().length() == 0) {
         throw new ValidateException(ApiError.MALFORMED_QUERY, "no fields specified");
      }
      String[] fields = selectFields.split(",");
      for (int i = 0; i < fields.length; i++) {
         String fieldName = fields[i].trim();
         fields[i] = fieldName;
      }
      if (!this.preservSelectColumnOrder) {
    	  Arrays.sort(fields);
      }
      return fields;
   }

   private String getPart(String qs, String start, String end) {

      String qsLower = qs.toLowerCase();

      boolean startFound = false;
      boolean endFound = false;

      int startIdx = 0;
      if (start != null) {
         startIdx = qsLower.indexOf(start);
         if (startIdx >= 0) {
            startIdx = startIdx + start.length();
            startFound = true;
         }
         else {
            startIdx = 0;
         }
      }

      int endIdx = -1;
      if (end != null) {
         endIdx = qsLower.indexOf(end, startIdx);
      }
      if (endIdx == -1) {
         endIdx = qsLower.length();
      }
      else {
         endFound = true;
      }

      if (!startFound && !endFound) {
         return null;
      }
      else {
         return qs.substring(startIdx, endIdx).trim();
      }
   }

   @Override
   public String toString() {
      return getZoql() + ": " + getQuery();
   }

   private String getZoql() {
      return zoql;
   }

   public String getHqlFields() {
      return hqlFields;
   }

   public String getHqlFrom() {
      return hqlFrom;
   }

   public String getHqlWhere() {
      return hqlWhere;
   }

   public ApiField[] getFields() {
      return fields;
   }

   public ApiClassMapper<Entity> getApiClassMapper() {
      return classMapper;
   }

   public List<Object> getParams() {
      if (specialQueryParam != null) {
         params.clear();
         // Arrays.asList can return immutable list and can cause issues.
         params = new ArrayList<Object>(java.util.Arrays.asList(specialQueryParam));
      }
      return params;
   }

   public boolean isSpecialQueryReturn() {
      return specialQueryReturn;
   }
   
   public boolean isSpecialQueryString(){
	   return specialQueryString!=null;
   }

   public Object[] getSpecialQueryResult() {
      return specialQueryResult;
   }
   
   public static class CaseSensitiveValueContainer{
      private String original = null;
      private byte[] bytes = null;
      public CaseSensitiveValueContainer(String original, byte[] b){
         this.original = original;
         this.bytes = b;
      }
      public String getOriginalValue(){
         return this.original;
      }
      public byte[] getByteValue(){
         return this.bytes;
      }
   }
   
   private static class HardCodeJoinApiField extends StaticApiField<String>{
	private String leftJoinString;
	public HardCodeJoinApiField(String leftJoinString){
		this.leftJoinString = leftJoinString;
	}
	@Override
	protected Pattern getPattern() {
		throw new RuntimeException("Can not run this operation against HardCodeJoinApiField.");
	}

	@Override
	public Object translateToSValue(Object value) throws ApiException {
		throw new RuntimeException("Can not run this operation against HardCodeJoinApiField.");
	}

	@Override
	public String translateToZValue(Object value) {
		throw new RuntimeException("Can not run this operation against HardCodeJoinApiField.");
	}

	@Override
	public ApiFieldType getApiFieldType() {
		throw new RuntimeException("Can not run this operation against HardCodeJoinApiField.");
	}
	
	public String getLeftJoinString(){
		return this.leftJoinString;
	}
	   
   }

   public String getZObject() {
      return this.zObject;
   }
}

package alvin.bef.framework.base.executor;


public interface Executor {
   <T> T execute(Executable<T> executable);
}

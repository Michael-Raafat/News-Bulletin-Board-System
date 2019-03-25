package remote;

public interface Task<T> {
	T execute(TaskType t);
}

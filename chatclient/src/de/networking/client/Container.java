package de.networking.client;

public class Container<T>
{
	private Class<T> tclass;
	private T t;

	Container(Class<T> tclass)
	{
		this.tclass = tclass;
	}

	Container(Class<T> tclass, T t)
	{
		this.t = tclass.cast(t);
	}

	Class<T> getElementClass()
	{
		return tclass;
	}

	void add(T t)
	{
		t = tclass.cast(t);
	}

	T getType()
	{
		return t;
	}
}

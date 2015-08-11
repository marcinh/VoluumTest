package com.voluum.framework;

/**
 * Interface helper for lambda expressions
 * @param <T>
 */
interface IAction<T>
{
    void execute(T argument);
}

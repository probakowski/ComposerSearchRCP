//******************************************************************
//
//  PeekingIterator.java
//  Copyright 2014 PSI AG. All rights reserved.
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package pl.robakowski.repository;

import java.util.Iterator;

public class LookBehindIterator< E > implements Iterator< E >
{
    private Iterator< E > iterator;
    private E current;

    public LookBehindIterator( Iterator< E > iterator )
    {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext()
    {
        return iterator.hasNext();
    }

    @Override
    public E next()
    {
        current = iterator.next();
        return current;
    }

    @Override
    public void remove()
    {
        iterator.remove();
    }

    public E last()
    {
        return current;
    }

}

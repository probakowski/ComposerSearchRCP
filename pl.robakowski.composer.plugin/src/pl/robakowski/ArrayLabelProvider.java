//******************************************************************
//
//  ArrayLabelProvider.java
//  Copyright 2014 PSI AG. All rights reserved.
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
// ******************************************************************

package pl.robakowski;

import org.eclipse.jface.viewers.ColumnLabelProvider;

public class ArrayLabelProvider< T > extends ColumnLabelProvider
{
    private int index;

    public ArrayLabelProvider( int index )
    {
        this.index = index;
    }

    @Override
    public String getText( Object aElement )
    {
        @SuppressWarnings( "unchecked" )
        T[] arr = (T[])aElement;
        return arr[ index ].toString();
    }
}

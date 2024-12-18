/*
 * sk.upjs.gursky.bplustree.KeyOffsetPair.java	ver 1.0, February 5th 2009
 *
 *	   Copyright 2009 Peter Gursky. All rights reserved.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sk.upjs.gursky.bplustree;

/**
 * Class used to send split data to parent node
 * 
 * @author Peter Gursky
 * @version 1.0, February 5th 2009
 * @see	    BPTree
 * @see	    BPKey
 *
 * @param <K> Key class used in inner nodes 
 */
class KeyOffsetPair<K extends BPKey<K>> {
	K	 key;
	long offset;
	
	KeyOffsetPair(K key, long offset) {
		this.key = key;
		this.offset = offset;
	}
}

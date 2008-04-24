/*
 * Copyright 1999 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.tools.example.debug.gui;

import java.util.*;

import javax.swing.AbstractListModel;

public class MonitorListModel extends AbstractListModel {

    private final List<String> monitors = new ArrayList<String>();

    MonitorListModel(Environment env) {

        // Create listener.
        MonitorListListener listener = new MonitorListListener();
        env.getContextManager().addContextListener(listener);

        //### remove listeners on exit!
    }

    public Object getElementAt(int index) {
        return monitors.get(index);
    }

    public int getSize() {
        return monitors.size();
    }

    public void add(String expr) {
        monitors.add(expr);
        int newIndex = monitors.size()-1;  // order important
        fireIntervalAdded(this, newIndex, newIndex);
    }

    public void remove(String expr) {
        int index = monitors.indexOf(expr);
        remove(index);
    }

    public void remove(int index) {
        monitors.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    public List<String> monitors() {
        return Collections.unmodifiableList(monitors);
    }

    public Iterator iterator() {
        return monitors().iterator();
    }

    private void invalidate() {
        fireContentsChanged(this, 0, monitors.size()-1);
    }

    private class MonitorListListener implements ContextListener {

        public void currentFrameChanged(CurrentFrameChangedEvent e) {
            invalidate();
        }
    }
}

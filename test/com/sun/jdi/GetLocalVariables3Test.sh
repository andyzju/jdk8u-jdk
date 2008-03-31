#!/bin/sh

#
# Copyright 2002 Sun Microsystems, Inc.  All Rights Reserved.
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
#
# This code is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License version 2 only, as
# published by the Free Software Foundation.
#
# This code is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# version 2 for more details (a copy is included in the LICENSE file that
# accompanied this code).
#
# You should have received a copy of the GNU General Public License version
# 2 along with this work; if not, write to the Free Software Foundation,
# Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
#
# Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
# CA 95054 USA or visit www.sun.com if you need additional information or
# have any questions.
#

#
#  @test
#  @bug 4448658
#  @summary javac produces the inconsistent variable debug in while loops.
#  @author Tim Bell
#
#  @run shell GetLocalVariables3Test.sh
#
classname=GetLocalVariables3Targ

createJavaFile()
{
    cat <<EOF > $classname.java.1
public class GetLocalVariables3Targ {
    public static void main(String[] args) {
        System.out.println("Howdy!");
        int i = 1, j, k;
        while ((j = i) > 0) {
            k = j; i = k - 1;    // @1 breakpoint
        }
        System.out.println("Goodbye from GetLocalVariables3Targ!");
    }
}
EOF
}

# This is called to feed cmds to jdb.
dojdbCmds()
{
   setBkpts @1
   runToBkpt @1
   cmd locals
   cmd cont
}

mysetup()
{
    compileOptions="-g"
    if [ -z "$TESTSRC" ] ; then
        TESTSRC=.
    fi

    for ii in . $TESTSRC $TESTSRC/.. ; do
        if [ -r "$ii/ShellScaffold.sh" ] ; then
            . $ii/ShellScaffold.sh 
            break
        fi
    done
}

mysetup

runit
jdbFailIfNotPresent "j = 1"
debuggeeFailIfNotPresent "Howdy"
debuggeeFailIfNotPresent "Goodbye from GetLocalVariables3Targ"
pass

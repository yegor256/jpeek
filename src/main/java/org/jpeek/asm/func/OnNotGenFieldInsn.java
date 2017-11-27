/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jpeek.asm.func;

/**
 * Function that will be called on visiting JVM instruction
 * to access field inside method. Function reacts only to
 * fields weren't generated by compiler.
 *
 * <p>After compilation byte code may contain more fields than you declared
 * in source code. For example all inner classes contain field {@code this$0},
 * it's a reference to outer class. You don't declare this one field
 * explicitly, but compiler will generate it implicitly. Sure generated fields
 * may will be implicitly used in methods. Use this one function to filter all
 * access field instructions inside some method and to react only on
 * instructions that access explicitly declared fields in the source code.</p>
 *
 * @author Sergey Karazhenets (sergeykarazhenets@gmail.com)
 * @version $Id$
 * @since 0.13
 */
public final class OnNotGenFieldInsn implements
    Proc4<Integer, String, String, String> {

    /**
     * Procedure that will be executed when method visitor will visit
     * access field instruction for field wasn't generated by compiler.
     */
    private final Proc4<Integer, String, String, String> proc;

    /**
     * Ctor.
     * @param proc Procedure to execute on field instruction visit event
     *  for field wasn't generated by compiler.
     */
    public OnNotGenFieldInsn(
        final Proc4<Integer, String, String, String> proc
    ) {
        this.proc = proc;
    }

    @Override
    // @checkstyle ParameterNumberCheck (1 line)
    public void exec(final Integer opcode, final String owner,
        final String name, final String desc
    ) throws Exception {
        if (!name.contains("$")) {
            this.proc.exec(opcode, owner, name, desc);
        }
    }
}
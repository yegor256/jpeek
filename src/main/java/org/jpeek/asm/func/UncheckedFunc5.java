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
 * {@link Func5} that doesn't throw checked {@link Exception}.
 *
 * @author Sergey Karazhenets (sergeykarazhenets@gmail.com)
 * @version $Id$
 * @param <T> Type of the first argument.
 * @param <R> Type of the second argument.
 * @param <S> Type of the third argument.
 * @param <X> Type of the fourth argument.
 * @param <Y> Type of the fifth argument.
 * @param <Z> Type of the function result.
 * @since 0.13
 */
public final class UncheckedFunc5<T, R, S, X, Y, Z> implements
    Func5<T, R, S, X, Y, Z> {

    /**
     * Original func.
     */
    private final Func5<T, R, S, X, Y, Z> func;

    /**
     * Ctor.
     * @param func Encapsulated func.
     */
    public UncheckedFunc5(final Func5<T, R, S, X, Y, Z> func) {
        this.func = func;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    // @checkstyle ParameterNumberCheck (1 line)
    public Z apply(final T first, final R second, final S third,
        final X fourth, final Y fifth
    ) {
        try {
            return this.func.apply(first, second, third, fourth, fifth);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(ex);
            // @checkstyle IllegalCatchCheck (1 line)
        } catch (final Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
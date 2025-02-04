/*
 * Copyright 2010-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jet;

public class Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> extends Tuple {
    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;
    public final T5 _5;
    public final T6 _6;
    public final T7 _7;
    public final T8 _8;
    public final T9 _9;
    public final T10 _10;
    public final T11 _11;
    public final T12 _12;
    public final T13 _13;
    public final T14 _14;

    public Tuple14(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        _1 = t1;
        _2 = t2;
        _3 = t3;
        _4 = t4;
        _5 = t5;
        _6 = t6;
        _7 = t7;
        _8 = t8;
        _9 = t9;
        _10 = t10;
        _11 = t11;
        _12 = t12;
        _13 = t13;
        _14 = t14;
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ", " + _7 + ", " + _8 + ", " + _9 + ", " + _10 + ", " + _11 + ", " + _12 + ", " + _13 + ", " + _14 + ")";
    }
    public final T1 get_1() {
        return _1;
    }
    public final T2 get_2() {
        return _2;
    }
    public final T3 get_3() {
        return _3;
    }
    public final T4 get_4() {
        return _4;
    }
    public final T5 get_5() {
        return _5;
    }
    public final T6 get_6() {
        return _6;
    }
    public final T7 get_7() {
        return _7;
    }
    public final T8 get_8() {
        return _8;
    }
    public final T9 get_9() {
        return _9;
    }
    public final T10 get_10() {
        return _10;
    }
    public final T11 get_11() {
        return _11;
    }
    public final T12 get_12() {
        return _12;
    }
    public final T13 get_13() {
        return _13;
    }
    public final T14 get_14() {
        return _14;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple14 t = (Tuple14) o;
        if (_1 != null ? !_1.equals(t._1) : t._1 != null) return false;
        if (_2 != null ? !_2.equals(t._2) : t._2 != null) return false;
        if (_3 != null ? !_3.equals(t._3) : t._3 != null) return false;
        if (_4 != null ? !_4.equals(t._4) : t._4 != null) return false;
        if (_5 != null ? !_5.equals(t._5) : t._5 != null) return false;
        if (_6 != null ? !_6.equals(t._6) : t._6 != null) return false;
        if (_7 != null ? !_7.equals(t._7) : t._7 != null) return false;
        if (_8 != null ? !_8.equals(t._8) : t._8 != null) return false;
        if (_9 != null ? !_9.equals(t._9) : t._9 != null) return false;
        if (_10 != null ? !_10.equals(t._10) : t._10 != null) return false;
        if (_11 != null ? !_11.equals(t._11) : t._11 != null) return false;
        if (_12 != null ? !_12.equals(t._12) : t._12 != null) return false;
        if (_13 != null ? !_13.equals(t._13) : t._13 != null) return false;
        if (_14 != null ? !_14.equals(t._14) : t._14 != null) return false;
        return true;
    }
    @Override
    public int hashCode() {
        int result = _1 != null ? _1.hashCode() : 0;
        result = 31 * result + (_2 != null ? _2.hashCode() : 0);
        result = 31 * result + (_3 != null ? _3.hashCode() : 0);
        result = 31 * result + (_4 != null ? _4.hashCode() : 0);
        result = 31 * result + (_5 != null ? _5.hashCode() : 0);
        result = 31 * result + (_6 != null ? _6.hashCode() : 0);
        result = 31 * result + (_7 != null ? _7.hashCode() : 0);
        result = 31 * result + (_8 != null ? _8.hashCode() : 0);
        result = 31 * result + (_9 != null ? _9.hashCode() : 0);
        result = 31 * result + (_10 != null ? _10.hashCode() : 0);
        result = 31 * result + (_11 != null ? _11.hashCode() : 0);
        result = 31 * result + (_12 != null ? _12.hashCode() : 0);
        result = 31 * result + (_13 != null ? _13.hashCode() : 0);
        result = 31 * result + (_14 != null ? _14.hashCode() : 0);
        return result;
    }

    @Override
    public void forEach(Function1<Object, Tuple0> fn) {
        fn.invoke(_1);
        fn.invoke(_2);
        fn.invoke(_3);
        fn.invoke(_4);
        fn.invoke(_5);
        fn.invoke(_6);
        fn.invoke(_7);
        fn.invoke(_8);
        fn.invoke(_9);
        fn.invoke(_10);
        fn.invoke(_11);
        fn.invoke(_12);
        fn.invoke(_13);
        fn.invoke(_13);
        fn.invoke(_14);
    }

    @Override
    public int size() {
        return 14;
    }
}

package enigma;

import java.util.HashMap;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Jeremy Ahn
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = new HashMap<Character, Character>();

        cycles = cycles.replaceAll("[)(]", "");
        String[] string = cycles.split(" ");

        for (int i = 0; i < alphabet().size(); i += 1) {
            _cycles.put(_alphabet.toChar(i), _alphabet.toChar(i));
        }

        for (String str: string) {
            addCycle(str);
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        char[] cycles = cycle.toCharArray();
        for (int i = 0; i < cycles.length; i += 1) {
            if (i == cycles.length - 1) {
                _cycles.put(cycles[i], cycles[0]);
            } else {
                _cycles.put(cycles[i], cycles[i + 1]);
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char result = alphabet().toChar(wrap(p));
        return alphabet().toInt(permute(result));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        for (char ch: _cycles.keySet()) {
            if (_cycles.get(ch) == _alphabet.toChar(wrap(c))) {
                return _alphabet.toInt(ch);
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _cycles.get(p);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (char ch: _cycles.keySet()) {
            if (_cycles.get(ch) == c) {
                return ch;
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (char ch: _cycles.keySet()) {
            if (ch == _cycles.get(ch)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** The cycles of this permutation. */
    private HashMap<Character, Character> _cycles;

}

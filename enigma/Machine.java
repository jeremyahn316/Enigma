package enigma;

import java.util.ArrayList;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Jeremy Ahn
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors.
     *  FIXME */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _myRotors = new Rotor[numRotors];

    }

    /** Return the number of rotor slots I have.
     * return 0; FIXME */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have.
     * return 0; FIXME */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting.
     *  //FIXME */
    void insertRotors(String[] rotors) {
        if (rotors.length != _numRotors) {
            throw new EnigmaException("Wrong size");
        }
        int count = 0;
        for (String name: rotors) {
            for (Rotor rotor : _allRotors) {
                if (name.equals(rotor.name())) {
                    _myRotors[count] = rotor;
                    break;
                }
            }
            count += 1;
        }
        if (_myRotors[_myRotors.length - 1] == null) {
            throw new EnigmaException("Moving rotor needed");
        }

        if (!_myRotors[_myRotors.length - 1].rotates()) {
            throw new EnigmaException("Last must be moving");
        }

        if (!_myRotors[0].reflecting()) {
            throw new EnigmaException("Has to be a reflector");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).
     *  // FIXME */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Setting length is incorrect");
        } else {
            for (int i = 1; i < numRotors(); i += 1) {
                _myRotors[i].set(_alphabet.toInt(setting.charAt(i - 1)));
            }
        }
    }
    /** Set the plugboard to PLUGBOARD.
     * FIXME */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine.
     *  FIXME */
    int convert(int c) {

        int result = _plugboard.permute(c);

        ArrayList<Integer> checkrotates = new ArrayList<>();
        for (int i = _myRotors.length - 2; i > 0; i -= 1) {
            if ((_myRotors[i].atNotch() && _myRotors[i - 1].rotates())
                    || _myRotors[i + 1].atNotch()) {
                checkrotates.add(i);
            }
        }

        for (int i = 0; i < checkrotates.size(); i += 1) {
            _myRotors[checkrotates.get(i)].advance();
        }

        _myRotors[_myRotors.length - 1].advance();

        for (int i = _myRotors.length - 1; i >= 0; i -= 1) {
            result = _myRotors[i].convertForward(result);
        }

        for (int i = 1; i < _myRotors.length; i += 1) {
            result = _myRotors[i].convertBackward(result);
        }

        result = _plugboard.permute(result);

        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly.
     *  FIXME */
    String convert(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i += 1) {
            int temp = convert(_alphabet.toInt(msg.charAt(i)));
            result += _alphabet.toChar(temp);
        }
        return result;
    }


    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Numver of rotors. */
    private int _numRotors;

    /** Number of pawls. */
    private int _pawls;

    /** Collection of all the rotors. */
    private Collection<Rotor> _allRotors;

    /** Plugboard of our Machine. */
    private Permutation _plugboard;

    /** List of rotors that are being used. */
    protected Rotor[] _myRotors;

}


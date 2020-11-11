package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Jeremy Ahn
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }

    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }

    @Override
    public boolean rotates() {
        return true;
    }


    @Override
    boolean atNotch() {
        return _notches.indexOf(permutation().alphabet().toChar(setting()))
                != -1;
    }

    /** The notches of this moving rotor. */
    private String _notches;

}

package derekahedron.invexp.client.util;

import org.joml.Vector2i;

/**
 * Tracks and manages scrolling within a looping list.
 */
public class Scroller {
    private double cumulHorizontal;
    private double cumulVertical;

    public Scroller() {
    }

    public Vector2i update(double horizontal, double vertical) {
        if (this.cumulHorizontal != 0.0 && Math.signum(horizontal) != Math.signum(this.cumulHorizontal)) {
            this.cumulHorizontal = 0.0;
        }

        if (this.cumulVertical != 0.0 && Math.signum(vertical) != Math.signum(this.cumulVertical)) {
            this.cumulVertical = 0.0;
        }

        this.cumulHorizontal += horizontal;
        this.cumulVertical += vertical;
        int i = (int) this.cumulHorizontal;
        int j = (int) this.cumulVertical;
        if (i == 0 && j == 0) {
            return new Vector2i(0, 0);
        } else {
            this.cumulHorizontal -= i;
            this.cumulVertical -= j;
            return new Vector2i(i, j);
        }
    }

    public static int scrollCycling(double amount, int selectedIndex, int total) {
        // When scrolling up (reserve traversal) if there is no selected index, scroll up from 0
        if (selectedIndex == -1 && amount > 0) {
            selectedIndex = 0;
        }

        selectedIndex -= (int) Math.signum(amount);
        selectedIndex = Math.floorMod(selectedIndex, total);
        return selectedIndex;
    }
}

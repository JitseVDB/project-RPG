import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class representing monsters in the game
 *
 * @invar	Each monster must have a properly spelled name.
 * 			| canHaveAsName(getName())
 *
 * @invar   Each monster must have a valid damage.
 *          | isValidDamage(getDamage());
 *
 * @author  Jitse Vandenberghe
 *
 * @version 1.1
 */
public class Monster extends Entity {

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new monster with the given name, damage, maximum hitpoints, type and initial items.
     *
     * @param   name
     *          The name of the monster.
     *
     * @param   maxHitPoints
     *          The base value of the monster.
     *
     * @param   damage
     *          Damage the monster deals.
     *
     * @param   initialItems
     *          A list of equipment items to randomly distribute over anchor points.
     *
     * @param   type
     *          The type of this mosnter (tough / thick / scaly).
     *
     * @effect  The monster is initialized as an entity with the given
     *          name and max hitpoints.
     *          | super(name, maxHitPoints)
     *
     * @post    The type of armor is set to the given type.
     *          | new.GetType = type
     *
     * @post    The maximal protection is set to the given maximal protection.
     *          | new.getMaximalProtection() == maximalProtection
     *
     * @effect  The new piece of armor is initialized with the maximal protection as current protection.
     *          | setCurrentProtection(maximalProtection)
     *
     * @effect  The new monster has the given damage.
     *          | setDamage(damage);
     *
     * @effect  The given items are randomly distributed over the anchor points,
     *          and the capacity is set accordingly.
     *          | distributeInitialItems(initialItems)
     *
     * @post    The new capacity is set to a number larger than the total weight of the initial items.
     *          | new.getCapacity() = rand.nextInt(Integer.MAX_VALUE) + getTotalWeight()
     *
     * @throws  IllegalArgumentException
     *          If the given damage is invalid.
     *          |!isValidDamage(damage)
     */
    public Monster(String name, int maxHitPoints, int damage, List<Equipment> initialItems, SkinType type) {
        super(name, maxHitPoints);

        if (!isValidDamage(damage))
            throw new IllegalArgumentException("Damage cannot be negative, must be below the maximum damage and must be a multiple of 7.");

        this.type = type;
        this.maximalProtection = type.getMaxProtection();
        setCurrentProtection(maximalProtection);

        setDamage(damage);

        distributeInitialItems(initialItems);

        Random rand = new Random();
        this.capacity = rand.nextInt(Integer.MAX_VALUE) + getTotalWeight();
    }

    /**********************************************************
     * Name
     **********************************************************/

    /**
     * Check whether the given name is a legal name for a monster.
     *
     * @param  	name
     *			The name to be checked
     *
     * @return	True if the given string is effective, not
     * 			empty, consisting only of letters, spaces
     * 			and apostrophes, and the name start
     * 			with a capital letter; false otherwise.
     * 			| result ==
     * 			|	(name != null) && name.matches("[A-Za-z '’]+") && (Character.isUpperCase(name.charAt(0)))
     */
    @Override @Raw
    public boolean canHaveAsName(String name) {
        return (name != null && name.matches("[A-Za-z '’]+") && Character.isUpperCase(name.charAt(0)));
    }

    /**********************************************************
     * Anchors
     **********************************************************/

    /**
     * Initializes a random number of anchor points for this monster.
     *
     * This method generates a random number between 0 (inclusive) and 100,
     * and adds that many anchor points to this monster. Each anchor point is initialized with null
     * as its name.
     *
     * @effect  Each newly created anchor point is added to this monster using addAnchorPoint.
     *          | for each i in 1..amount:
     *          |   addAnchorPoint(new AnchorPoint(anchor_i))
     *
     * @post    The total number of anchor points for this monster will increase by the generated amount.
     *          | getNbAnchorPoints() == amount
     *
     * @note    We chose limit 100, because of performance issues otherwise.
     */
    @Override
    public void initializeAnchorPoints() {
        Random random = new Random();
        int amount = random.nextInt(101);

        for (int i = 1; i <= amount; i++) {
            addAnchorPoint(new AnchorPoint("anchor_" + i));
        }
    }

    /**
     * Distribute the given items across this monster's anchor points in order.
     *
     * The items are assigned to anchor points in the order they appear in the list.
     * If there are more items than anchor points, only the first items up to the number of anchor points
     * will be assigned. If there are fewer items than anchor points, some anchor points will remain empty.
     *
     * @param items
     *        The items to distribute.
     *
     * @post  Each item is assigned to one of this monster's anchor points.
     *          | for each i in 0 .. min(items.size(), getNbAnchorPoints()):
     *          |   getAnchorPointAt(i).setItem(items.get(i))
     *
     */
    public void distributeInitialItems(List<Equipment> items) {
        int maxItems = Math.min(items.size(), getNbAnchorPoints());

        for (int i = 1; i <= maxItems; i++) {
            Equipment item = items.get(i-1);
            this.capacity += item.getWeight(); // zodat monster altijd item kan dragen
            item.setOwner(this);
        }
    }

    /**
     * Checks whether item can be legally added to anchorpoint
     *
     * @param item
     *          item to check
     * @param anchorPoint
     *          anchorpoint to check
     * @return  returns true always, because every item can be legally added to an anchorpoint in monster
     *
     */
    @Override @Raw
    public boolean canHaveAsItemAt(Equipment item, AnchorPoint anchorPoint ) {
        return true;
    }


    /**********************************************************
     * Damage
     **********************************************************/

    /**
     * Variable referencing the maximum amount of damage a monster can deal.
     */
    private static final int maximumDamage = 100;

    /**
     * Return the maximum damage a monster can deal.
     */
    @Basic @Immutable
    public static int getMaximumDamage() {
        return maximumDamage;
    }

    /**
     * Variable registering the damage of this monster
     */
    private int damage;

    /**
     * Return the damage of this monster.
     */
    @Raw @Basic
    public int getDamage() {
        return damage;
    }

    /**
     * Set the damage of this monster to the given damage.
     *
     * @param   damage
     *          The new damage for this monster.
     *
     * @pre     The given damage must be legal.
     *          | isValidDamage(damage)
     * @post    The given damage is registered as the damage of this monster.
     *          | new.getDamage() == damage
     */
    @Raw
    public void setDamage(int damage) {this.damage = damage;}

    /**
     * Check whether the given damage is valid for this monster.
     *
     * @param   damage The damage to check.
     *
     * @return  True if and only if the given damage is positive, does not exceed the maximum allowed damage,
     *          and is a multiple of 7.
     *          | result == (damage > 0 && damage <= maximumDamage && damage % 7 == 0)
     */
    @Raw
    public boolean isValidDamage(int damage) {
        return damage > 0 && damage <= maximumDamage && damage % 7 == 0;
    }


    /**********************************************************
     * Protection
     **********************************************************/

    /**
     * Variable referencing the maximum protection this monster has as natural protection.
     */
    private final int maximalProtection;

    /**
     * Variable referencing the current protection this monster has as natural protection.
     */
    private int currentProtection;

    /**
     * Return the maximum protection this monster has as natural protection.
     */
    @Basic @Immutable
    public int getMaximalProtection() {
        return maximalProtection;
    }

    /**
     * Check whether the given maximal protection is a valid maximal protection for this monster.
     *
     * @param   maximalProtection
     *          The maximal protection to check.
     *
     * @return  True if and only if the given maximal protection is positive and does not exceed 100.
     *          | result == (value > 0 && value <= 100)
     */
    @Raw
    public boolean isValidMaximalProtection(int maximalProtection) {
        return maximalProtection > 0 && maximalProtection <= 100;
    }

    /**
     * Return the current protection this monster can provide
     */
    @Basic
    public int getCurrentProtection() {
        return currentProtection;
    }

    /**
     * Set the current protection of this monster to the given protection.
     *
     * @param   currentProtection
     *          The new current protection for this monster.
     *
     * @post    The given current protection is registered as the current protection of this piece of armor.
     *          | new.getCurrentProtection() == currentProtection
     *
     * @throws  IllegalArgumentException
     *          If the given current protection is invalid.
     *          |!isValidCurrentProtection(currentProtection)
     */
    public void setCurrentProtection(int currentProtection)
            throws IllegalArgumentException {
        if (!isValidCurrentProtection(currentProtection))
            throw new IllegalArgumentException("The current protection must be between 0 and " + maximalProtection);
        this.currentProtection = currentProtection;
    }

    /**
     * Check whether the given maximal protection is a valid maximal protection for this monster.
     *
     * @param   currentProtection
     *          The current protection to check.
     *
     * @return  True if and only if the given current protection is greater or equal to zero and does not exceed the maximal protection.
     *          | result == (value >= 0 && value <= maximalProtection)
     */
    @Raw
    public boolean isValidCurrentProtection(int currentProtection) {
        return currentProtection >= 0 && currentProtection <= maximalProtection;
    }

    /**********************************************************
     * Armor Type
     **********************************************************/

    /**
     * Variable referencing the type of skin (Tin/Bronze)
     */
    private final SkinType type;

    /**
     * Return the type of skin of this monster.
     */
    @Basic @Immutable
    public SkinType getType() {
        return type;
    }

    /**********************************************************
     * Hit
     **********************************************************/

    /**
     * Performs a single hit from this monster to the given opponent.
     *
     * @param opponent
     *        The entity that is the target of the hit.
     *
     * @effect  The monster will loot the defeated opponent when a fatal blow is dealt.
     *          | if opponent.getHitPoints() - damage <= 0 then loot(opponent)
     *
     * @post    If the hit is successful and fatal, opponent's hit points are zero.
     *          | if opponent.getHitPoints() - damage <= 0 then opponent.getHitPoints() == 0
     *
     * @post    If the hit is successful but not fatal, opponent's hit points decrease by the damage amount.
     *          | if opponent.getHitPoints() - damage > 0 then opponent.getHitPoints() == old opponent.getHitPoints() - getDamage()
     *
     * @post    If the hit misses, opponent's hit points remain unchanged.
     *          | if impact < opponent.getProtection() then opponent.getHitPoints() == old opponent.getHitPoints()
     */
    public void hit(Entity opponent) {
        Random random = new Random();

        int impact = random.nextInt(101); // between 0 (inclusive) and bound value '101' (exclusive)


        if (impact >= getHitPoints()) {
            impact = getHitPoints();
        }


        if (impact >= ((Hero)opponent).getRealProtection()) {
            // land a succesful hit
            int damage = getDamage();
            int newHitPoints = opponent.getHitPoints() - damage;

            if (newHitPoints <= 0) {
                // Final blow: opponent defeated
                opponent.setHitPoints(0);

                // Let monster take any possessions and destroy any weapons/armor left behind
                loot(opponent);

            } else {
                // Regular hit, adjust life points
                opponent.setHitPoints(newHitPoints);
            }
        } else {
            // Miss the target: no effect
        }
    }

    /**
     * Attempt to loot items from a defeated opponent.
     *
     * This method is called after the monster deals a fatal blow.
     * The monster attempts to pick up as many weapons and armors as possible from the opponent,
     * given its available anchor points.
     *
     * Weapons and armors that are not looted are destroyed.
     * Backpacks and purses that are not looted remain behind on the ground.
     *
     * @param   opponent
     *          The defeated opponent from which to loot.
     *
     * @effect  All items are removed from the opponent and collected for consideration.
     *          | for each anchor point i:
     *          |     item = opponent.getAnchorPointAt(i).getItem()
     *          |     if item != null:
     *          |         item.setOwner(null)
     *          |         opponent.getAnchorPointAt(i).setItem(null)
     *
     * @effect  Loots shiny weapons and armors first if there are available anchor points.
     *          | if item.isShiny() && (item instanceof Weapon || item instanceof Armor)
     *          |     and hasFreeAnchorPoint():
     *          |     addToAnchorPoint(item) and item.setOwner(this)
     *
     * @effect  After all shiny items are looted, loots non-shiny weapons and armors if there is space.
     *          | if !item.isShiny() && (item instanceof Weapon || item instanceof Armor)
     *          |     and hasFreeAnchorPoint():
     *          |     addToAnchorPoint(item) and item.setOwner(this)
     *
     * @effect  All weapons and armors not looted are destroyed.
     *          | if item instanceof Weapon or Armor and not looted:
     *          |     item.destroy()
     *
     * @post    All looted items are removed from the opponent.
     *          | for each item looted:
     *          |     item.getOwner() == this
     *          |     opponent does not have the item anymore in any anchor point
     *
     * @post    All non-looted weapons and armors from the opponent are destroyed.
     *          | for each non-looted weapon or armor item:
     *          |     item.isDestroyed() == true
     *
     * @post    The monster may have looted up to its available capacity.
     *          | getNbItemsCarried() <= getNbAnchorPoints()
     */
    public void loot(Entity opponent) {
        List<Equipment> shinyLoot = new ArrayList<>();
        List<Equipment> nonShinyLoot = new ArrayList<>();

        // Step 1: Collect all items and sort between shiny and nonShiny
        for (int i = 1; i <= opponent.getNbAnchorPoints(); i++) {
            AnchorPoint ap = opponent.getAnchorPointAt(i);
            Equipment item = ap.getItem();
            if (item != null) {
                if (item.isShiny()) {
                    shinyLoot.add(item);
                } else {
                    nonShinyLoot.add(item);
                }
            }
        }


        // Step 2: Try to loot shiny weapons/armors
        for (Equipment item : shinyLoot) {
            try {
                item.setOwner(this); // stel eigenaar in en checkt ook of dit mag/kan
            } catch (IllegalArgumentException e) {
                // Destroy non-looted weapons and armors only
                if ((item instanceof Weapon || item instanceof Armor)) {
                    // Destroy non-looted weapons and armors only
                    item.destroy();
                }
                // Backpacks and purses not looted remain on the ground, do nothing
            }
        }

        // Step 3: Try to loot non-shiny weapons/armors or backpacks and purses
        for (Equipment item : nonShinyLoot) {
            try {
                item.setOwner(this); // stel eigenaar in en checkt ook of dit mag/kan
            } catch (IllegalArgumentException e) {
                // Destroy non-looted weapons and armors only
                if ((item instanceof Weapon || item instanceof Armor)) {
                    // Destroy non-looted weapons and armors only
                    item.destroy();
                }
                // Backpacks and purses not looted remain on the ground, do nothing
            }
        }
    }
}
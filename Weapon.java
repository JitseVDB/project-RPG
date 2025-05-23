import java.util.*;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of weapons, involving damage.
 *
 * @invar   Each weapon must have a valid damage.
 *          | isValidDamage(getDamage());
 *
 * @note    Only the additional invariants that are not already defined in the
 *          superclass `Equipment` need to be specified.
 *
 * @author  Jitse Vandenberghe
 * @version 1.0
 */
public class Weapon extends Equipment {

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new weapon with the given weight, base value, and damage.
     *
     * @param   weight
     *          The weight of the weapon.
     *
     * @param   damage
     *          The damage this weapon can inflict.
     *
     * @effect  The weapon is initialized as a piece of equipment.
     *          (weight, base value are set and an identification number is generated and assigned)
     *          | super(weight, 0)
     *
     * @effect  The new weapon has the given damage.
     *          | setDamage(damage);
     *
     * @post    The shininess is set to true.
     *          | new.isShiny() = true
     *
     * @throws  IllegalArgumentException
     *          If the given damage is invalid.
     *          |!isValidDamge(damage)
     */
    public Weapon(int weight, int damage) {
        super(weight, 0);

        if (!isValidDamage(damage))
            throw new IllegalArgumentException("Damage cannot be negative, must be below the maximum damage and must be a multiple of 7.");

        setDamage(damage);
        this.isShiny = true;
    }

    /**********************************************************
     * Identification
     **********************************************************/

    /**
     * Check whether the given identification number is a valid identification number for this weapon.
     *
     * @param   equipmentType
     *          The class type of the equipment for which the identification number is being validated.
     *
     * @param   identification
     *          The identification number to check.
     *
     * @return  True if the identification number satisfies the superclasses validity conditions and
     *          is divisible by both 2 and 3, false otherwise.
     *          | result == super.isValidIdentification(equipmentType, identification)
     *          |        && (identification % 2 == 0)
     *          |        && (identification % 3 == 0)
     */
    @Raw @Override
    public boolean canHaveAsIdentification(Class<?> equipmentType, long identification) {
        return super.canHaveAsIdentification(equipmentType, identification)
                // The identification number must be divisible by 2 and 3.
                && identification % 2 == 0
                && identification % 3 == 0;
    }

    /**
     * Generates a valid and unique identification number for this weapon.
     *
     * @return  A non-negative and unique identification number divisble by 2 and 3 that satisfies the conditions defined by canHaveAsIdentification.
     *          | canHaveAsIdentification(this.getClass(), result)
     *
     * @post   The returned identification number is guaranteed to satisfy the validity conditions stated in canHaveAsIdentification.
     *         | canHaveAsIdentification(this.getClass(), result)
     *
     * @note   The identification number is not automatically added to the registry; this must be done separately
     *         (via addIdentification()).
     */
    @Override
    public long generateIdentification() {
        Random random = new Random();
        long possibleID = Math.abs(random.nextLong())*6;

        // Keep generating a new number until we find a valid identification number
        while (!canHaveAsIdentification(this.getClass(), possibleID)) {
            possibleID = Math.abs(random.nextLong())*6; // Generate a new number if it's not prime
        }

        return possibleID;
    }

    /**********************************************************
     * Damage - nominal programming
     **********************************************************/

    /**
     * Variable referencing the damage of this weapon
     */
    private int damage = 0;

    /**
     * Variable referencing the maximum amount of damage a weapon can deal.
     */
    private static final int maximumDamage = 100;

    /**
     * Return the damage of this weapon.
     */
    @Basic
    public int getDamage() {
        return damage;
    }

    /**
     * Set the damage of this weapon to the given damage.
     *
     * @param   damage
     *          The new damage for this weapon.
     *
     * @pre     The equipment is not destroyed.
     *          | !isDestroyed()
     *
     * @pre     The given damage must be legal.
     *          | isValidDamage(damage)
     *
     * @post    The given damage is registered as the damage of this weapon.
     *          | new.getDamage() == damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Return the maximum damage a weapon can deal.
     */
    @Basic @Immutable
    public static int getMaximumDamage() {
        return maximumDamage;
    }

    /**
     * Check whether the given damage is valid for this weapon.
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
     * Value
     **********************************************************/

    /**
     * Returns the maximum value of a weapon.
     */
    @Override @Basic @Immutable
    public int getMaximumValue() {
        return 200;
    }

    /**
     * Variable referencing the value per damage unit for a weapon.
     */
    private static final int valuePerDamageUnit = 2; // Default value, 2 dukaten per damage unit

    /**
     * Returns the value per damage unit for a weapon, in dukaten.
     */
    @Basic @Immutable
    public int getValuePerDamageUnit() {
        return valuePerDamageUnit;
    }

    /**
     * Calculate the current value of this weapon.
     *
     * @return  The calculated current value, guaranteed to be positive and at most 200.
     *          | result == damage * valuePerDamageUnit
     */
    protected int calculateCurrentValue() {
        return getDamage() * getValuePerDamageUnit();
    }

    /**********************************************************
     * Condition
     **********************************************************/

    /**
     * Set this weapon to destroyed and set its damage to zero.
     *
     * @effect  The damage of the purse is set to zero.
     *          | setDamage(0)
     *
     * @effect  The condition is set to DESTROYED
     *          | setCondition(Condition.DESTROYED)
     */
    @Override @Model
    void destroy() {
        setDamage(0);
        setCondition(Condition.DESTROYED);
    }
}




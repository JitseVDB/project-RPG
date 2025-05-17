import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A JUnit (5) test class for testing the non-private methods of the Monster class.
 *
 * @author Ernest De Gres
 * @version 1.1
 */

public class MonsterTest {

    // MONSTER
    private static Monster monster_A;

    // OPPONENT
    private static Hero hero_A;

    // EQUIPMENT
    private static Weapon weapon_A;
    private static Weapon weapon_B;
    private static Armor armor_A;
    private static Armor armor_B;
    private static Backpack backpack_A;
    private static Backpack backpack_B;
    private static Purse purse_A;
    private static Purse purse_B;

    // LISTS
    private List<Equipment> items;

    @BeforeEach
    public void setUpMonster() {
        hero_A = new Hero("Ben", 100, 4);

        weapon_A = new Weapon(40, 49);
        weapon_B = new Weapon(40, 49);
        armor_A = new Armor(30, 80, ArmorType.BRONZE);
        armor_B = new Armor(30, 80, ArmorType.BRONZE);
        backpack_A = new Backpack(30, 60, 100);
        backpack_B = new Backpack(30, 60, 100);
        purse_A = new Purse(10, 50);
        purse_B = new Purse(10, 50);

        items = new ArrayList<>();
        items.add(weapon_A);
        items.add(armor_A);
        items.add(backpack_A);
        items.add(purse_A);

        monster_A = new Monster("Tom", 70, 49, items, SkinType.SCALY);
        // 1. Adds 6 anchorpoints because we have to have 2 free anchorpoints for the tests and have to make sure every item also has an anchorpoint to be added on.
        monster_A.addAnchorPoint(new AnchorPoint("anchor_1"));
        monster_A.addAnchorPoint(new AnchorPoint("anchor_2"));
        monster_A.addAnchorPoint(new AnchorPoint("anchor_3"));
        monster_A.addAnchorPoint(new AnchorPoint("anchor_4"));
        monster_A.addAnchorPoint(new AnchorPoint("anchor_5"));
        monster_A.addAnchorPoint(new AnchorPoint("anchor_6"));
    }

    /**
     * CONSTRUCTORS
     */

    @Test
    void testConstructor_ValidArguments_ShouldInitializeFields() {
        // 1. effect of super(name, maxHitPoints)
        // 1.1 postcondition on name
        assertEquals("Tom", monster_A.getName());
        // 1.2 postcondition on hitpoints
        assertEquals(70, monster_A.getMaxHitPoints());
        assertEquals(67, monster_A.getHitPoints());
        // 1.3 postcondition on fighting status
        assertFalse(monster_A.isFighting());
        // 1.4 effect of initializeAnchorPoint
        assertTrue(monster_A.getNbAnchorPoints() >= 0 && monster_A.getNbAnchorPoints() <= 100);

        // 2. postcondition on skin type
        assertEquals(SkinType.SCALY, monster_A.getType());

        // 3. postcondition on maximal protection
        assertEquals(SkinType.SCALY.getMaxProtection(), monster_A.getMaximalProtection());

        // 4. postcondition on current protection
        assertEquals(SkinType.SCALY.getMaxProtection(), monster_A.getMaximalProtection());

        // 5. postcondition on damage
        assertEquals(49, monster_A.getDamage());

        // 7. effect of distributeInitial
        // 7.1 postcondition on items and anchorpoints
        assertEquals(weapon_A, monster_A.getAnchorPointAt(1).getItem());
        assertEquals(armor_A, monster_A.getAnchorPointAt(2).getItem());
        assertEquals(backpack_A, monster_A.getAnchorPointAt(3).getItem());
        assertEquals(purse_A, monster_A.getAnchorPointAt(4).getItem());

        // 8. postcondition on capacity
        assertTrue(monster_A.getCapacity() >= monster_A.getTotalWeight());
    }

    @Test
    void testConstructor_InvalidDamage_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Monster("InvalidDamage", 70, -1 , items, SkinType.SCALY);
        });
    }

    /**
     * NAME
     */

    @Test
    void testCanHaveAsName_allCases() {
        // 1. Valid cases
        assertTrue(monster_A.canHaveAsName("Tom"));
        assertTrue(monster_A.canHaveAsName("O'Tom"));
        // 2. Invalid cases
        assertFalse(monster_A.canHaveAsName(null));
        assertFalse(monster_A.canHaveAsName("tom"));
        assertFalse(monster_A.canHaveAsName("tom123"));
    }

    /**
     * ANCHORS
     */

    @Test
    void testInitializeAnchorPoints_ShouldCreateAnchorPoints() {
        assertTrue(monster_A.getNbAnchorPoints() >= 0 && monster_A.getNbAnchorPoints() <= 100);
    }

    @Test
    void testDistributeInitialItems_ShouldAssignToAnchorPoints() {
        // 2. Items distributed in initialization
        assertEquals(weapon_A, monster_A.getAnchorPointAt(1).getItem());
        assertEquals(armor_A, monster_A.getAnchorPointAt(2).getItem());
        assertEquals(backpack_A, monster_A.getAnchorPointAt(3).getItem());
        assertEquals(purse_A, monster_A.getAnchorPointAt(4).getItem());
    }

    @Test
    void testCanHaveAsItem_NoEmptyAnchorPoints_ShouldReturnFalse() {
        // fills all anchorpoints
        while (monster_A.hasFreeAnchorPoint()) {
            Weapon newWeapon = new Weapon(40, 49);
            assertTrue(monster_A.canHaveAsItem(newWeapon));
            newWeapon.setOwner(monster_A);
        }
        assertFalse(monster_A.canHaveAsItem(weapon_B));
    }

    @Test
    void testCanHaveAsItem_ExceedingCapacity_ShouldReturnFalse() {
        Weapon weapon_C = new Weapon(monster_A.getCapacity(), 49);
        assertFalse(monster_A.canHaveAsItem(weapon_C));
    }

    // DAMAGE

    @Test
    public void testsetDamage_allCases() {
        monster_A.setDamage(7);
        // 1. postcondition on damage
        assertEquals(7, monster_A.getDamage());
    }

    @Test
    void testGetMaximumDamage_ShouldReturnConstant() {
        assertEquals(100, Monster.getMaximumDamage());
    }

    @Test
    void testSetAndGetDamage_ShouldReturnValue() {
        monster_A.setDamage(49);
        assertEquals(49, monster_A.getDamage());
    }

    @Test
    public void testIsValidDamage_allCases() {

        // 1. positive
        assertTrue(monster_A.isValidDamage(7));
        assertFalse(monster_A.isValidDamage(0));
        assertFalse(monster_A.isValidDamage(-7));

        // 2. less than or equal maximum damage
        int invalidDamage = Weapon.getMaximumDamage() + (7 - Weapon.getMaximumDamage() % 7);
        assertFalse(monster_A.isValidDamage(invalidDamage));

        // 3. Divisible by 7
        assertTrue(monster_A.isValidDamage(49));
        assertFalse(monster_A.isValidDamage(50));
    }

    /**
     * PROTECTION
     */

    @Test
    public void testIsValidMaximalProtection_allCases() {
        assertTrue(monster_A.isValidMaximalProtection(SkinType.TOUGH.getMaxProtection()));
        assertTrue(monster_A.isValidMaximalProtection(SkinType.THICK.getMaxProtection()));
        assertTrue(monster_A.isValidMaximalProtection(SkinType.SCALY.getMaxProtection()));
        assertFalse(monster_A.isValidMaximalProtection(-1));
        assertFalse(monster_A.isValidMaximalProtection(0));
        assertTrue(monster_A.isValidMaximalProtection(100));
        assertFalse(monster_A.isValidMaximalProtection(101));
    }

    @Test
    public void testSetCurrentProtection_shouldSetField() {
        monster_A.setCurrentProtection(monster_A.getMaximalProtection());
        assertEquals(monster_A.getMaximalProtection(), monster_A.getCurrentProtection());
        monster_A.setCurrentProtection(0);
        assertEquals(0, monster_A.getCurrentProtection());
    }

    @Test
    public void testSetCurrentProtection_AboveMaximum_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> monster_A.setCurrentProtection(monster_A.getMaximalProtection()+1));
    }

    @Test
    public void testSetCurrentProtection_BelowMinimum_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> monster_A.setCurrentProtection(-(monster_A.getMaximalProtection())));
    }

    @Test
    public void testisValidCurrentProtection_allCases() {
        assertTrue(monster_A.isValidCurrentProtection(SkinType.TOUGH.getMaxProtection()));
        assertTrue(monster_A.isValidCurrentProtection(SkinType.THICK.getMaxProtection()));
        assertTrue(monster_A.isValidCurrentProtection(SkinType.SCALY.getMaxProtection()));
        assertFalse(monster_A.isValidCurrentProtection(-1));
        assertTrue(monster_A.isValidCurrentProtection(0));
        assertTrue(monster_A.isValidCurrentProtection(30));
        assertFalse(monster_A.isValidCurrentProtection(ArmorType.BRONZE.getMaxProtection()+1));
    }

    /**
     * HIT
     */

    @Test
    void testHit_FatalBlow_ShouldHaveZeroHitPoints() {
        // 1. fatal hit
        hero_A.setProtection(0); // decrease protection so hit is succesfull
        monster_A.setDamage(10000); // fatal
        monster_A.hit(hero_A);

        // 2. opponent has zero hitpoints
        assertEquals(0, hero_A.getHitPoints());
    }

    @Test
    void testHit_MissHit_ShouldNotChangeHitPoints() {
        hero_A.setHitPoints(79);
        monster_A.setDamage(14);

        // increase protection so hit fails
        hero_A.setProtection(999);
        monster_A.hit(hero_A);

        assertEquals(79, hero_A.getHitPoints());
    }

    @Test
    void testHit_NonFatal_ShouldReduceHitPoints() {
        hero_A.setHitPoints(80);
        monster_A.setDamage(10);
        hero_A.setProtection(0);

        monster_A.hit(hero_A);
        assertTrue(hero_A.getHitPoints() < 80);
    }


    // Loot

    @Test
    void testLoot_LootedItemsRemovedFromOpponent() {
        weapon_B.setOwner(hero_A);
        armor_B.setOwner(hero_A);

        // Had 2 free anchorpoints added
        monster_A.loot(hero_A);

        // Postcondition: all looted items are removed from the opponent
        assertFalse(hero_A.hasAsItem(weapon_B));
        assertFalse(hero_A.hasAsItem(armor_B));
    }

    @Test
    void testLoot_OwnerSetToMonster() {
        weapon_B.setOwner(hero_A);
        armor_B.setOwner(hero_A);

        // Had 2 free anchorpoints added
        monster_A.loot(hero_A);

        assertTrue(monster_A.hasAsItem(weapon_B) && weapon_B.getOwner() == monster_A);
        assertTrue(monster_A.hasAsItem(armor_B) && armor_B.getOwner() == monster_A);
    }

    @Test
    void testLoot_DestroyNonLootedWeaponsAndArmors() {
        // fills all anchorpoints
        while (monster_A.hasFreeAnchorPoint()) {
            Weapon newWeapon = new Weapon(40, 49);
            newWeapon.setOwner(monster_A);
        }

        weapon_B.setOwner(hero_A);
        armor_B.setOwner(hero_A);

        monster_A.loot(hero_A);

        assertTrue(weapon_B.isDestroyed());
        assertTrue(armor_B.isDestroyed());
    }

    @Test
    void testLoot_DoesNotDestroyNonLootedBackpacksAndPurses() {
        // fills all anchorpoints
        while (monster_A.hasFreeAnchorPoint()) {
            Weapon newWeapon = new Weapon(40, 49);
            newWeapon.setOwner(monster_A);
        }

        backpack_B.setOwner(hero_A);
        purse_B.setOwner(hero_A);

        monster_A.loot(hero_A);

        assertFalse(backpack_B.isDestroyed());
        assertFalse(purse_B.isDestroyed());
    }

    @Test
    void testLoot_ShinyItemsLootedFirst() {
        // Set armor not shiny
        armor_B.setShiny(false);

        weapon_B.setOwner(hero_A);
        armor_B.setOwner(hero_A);

        // fills all anchorpoints
        while (monster_A.hasFreeAnchorPoint()) {
            Weapon newWeapon = new Weapon(40, 49);
            newWeapon.setOwner(monster_A);
        }

        // adds one free anchorpoints
        monster_A.addAnchorPoint(new AnchorPoint(null));

        monster_A.loot(hero_A);

        // shiny weapon looted, armor not because no space.
        assertTrue(monster_A.hasAsItem(weapon_B));
        assertFalse(monster_A.hasAsItem(armor_B));
    }

}
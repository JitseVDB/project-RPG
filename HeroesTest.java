import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


/**
 * A JUnit (5) test class for testing the non-private methods of the Hero class.
 *
 * @author Ernest De Gres
 * @version 1.1
 */

public class HeroesTest {


    // HERO
    private static Hero hero_A;
    private static Hero hero_B;

    // OPPONENT
    private static Monster monster_A;

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
        weapon_A = new Weapon(40, 98);
        weapon_B = new Weapon(40, 98);
        armor_A = new Armor(30, 80, ArmorType.BRONZE);
        armor_B = new Armor(30, 80, ArmorType.BRONZE);
        backpack_A = new Backpack(30, 60, 100);
        backpack_B = new Backpack(30, 60, 100);
        purse_A = new Purse(10, 50);
        purse_B = new Purse(10, 50);


        hero_A = new Hero("Ben", 13, 20.0);
        hero_B = new Hero("Bert", 13, 20.0, weapon_B, armor_B, purse_B, backpack_B);


        items = new ArrayList<>();
        items.add(weapon_A);
        items.add(armor_A);
        items.add(backpack_A);
        items.add(purse_A);

        monster_A = new Monster("Tom", 70, 49, items, SkinType.SCALY);
    }
    /********************************************************************
     *                      CONSTRUCTOR TEST
     ********************************************************************/

    @Test
    public void testFirstConstructor_ValidArguments_ShouldInitializeFields() {
        assertEquals("Ben", hero_A.getName());
        assertEquals(13, hero_A.getMaxHitPoints());
        assertEquals(13, hero_A.getHitPoints()); // geen prime correctie nodig
        assertEquals(20.0, hero_A.getIntrinsicStrength(), 0.001);
        assertEquals(10, hero_A.getProtection());
        assertEquals(400, hero_A.getCapacity());
        assertFalse(hero_A.isFighting());
        assertNull(hero_A.getLeftHandWeapon());
        assertNull(hero_A.getRightHandWeapon());
        assertNull(hero_A.getArmor());
    }

    @Test
    public void testFirstConstructor_InvalidStrength_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Hero("Ben", 11, 0.0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Hero("Ben", 11, -5.0);
        });

    }

    @Test
    public void testSecondConstructor_ValidArguments_ShouldInitializeFields() {
        // Check basisvelden
        assertEquals("Bert", hero_B.getName());
        assertEquals(13, hero_B.getMaxHitPoints());
        assertEquals(13, hero_B.getHitPoints()); // geen prime correctie nodig
        assertEquals(20.0, hero_B.getIntrinsicStrength(), 0.001);
        assertEquals(10, hero_B.getProtection());
        assertEquals(400, hero_B.getCapacity());
        assertFalse(hero_B.isFighting());

        // Controleer of items effectief op ankers hangen
        assertTrue(hero_B.getAnchors().containsValue(weapon_B));
        assertTrue(hero_B.getAnchors().containsValue(armor_B));
        assertTrue(hero_B.getAnchors().containsValue(purse_B));
        assertTrue(hero_B.getAnchors().containsValue(backpack_B));

        // Controleer of items de juiste eigenaar hebben
        assertEquals(hero_B, weapon_B.getOwner());
        assertEquals(hero_B, armor_B.getOwner());
        assertEquals(hero_B, purse_B.getOwner());
        assertEquals(hero_B, backpack_B.getOwner());
    }


    /********************************************************************
     *                          NAME TEST
     ********************************************************************/


    @Test
    void testCanHaveAsName_AllCases() {
        // valid cases
        assertTrue(hero_A.canHaveAsName("Hendrik"));
        assertTrue(hero_A.canHaveAsName("Ben Bert Tom"));
        assertTrue(hero_A.canHaveAsName("Ben: Bert"));
        assertTrue(hero_A.canHaveAsName("Ben'Bert'Tom"));

        // invalid cases
        assertFalse(hero_A.canHaveAsName(null));
        assertFalse(hero_A.canHaveAsName(""));
        assertFalse(hero_A.canHaveAsName("ben"));
        assertFalse(hero_A.canHaveAsName("O'Ben D'Tom Mc'Bert"));
        assertFalse(hero_A.canHaveAsName("Ben:Bert"));
        assertFalse(hero_A.canHaveAsName("Jean-Luc#1"));
        assertFalse(hero_A.canHaveAsName("Hero 1"));
        assertFalse(hero_A.canHaveAsName("Captain:"));
    }


    /********************************************************************
     *                       PROTECTION TEST
     ********************************************************************/

    @Test
    void testSetAndGetProtection_ShouldReturnValue() {
        hero_A.setProtection(50);
        assertEquals(50, hero_A.getProtection());
    }

    @Test
    void testIsValidProtection_AllCases() {
        // valid case
        assertTrue(Hero.isValidProtection(5));

        // invalid case
        assertFalse(Hero.isValidProtection(-1));
    }

    @Test
    void testGetRealProtection_WithArmor_ShouldAddArmorProtection() {
        hero_A.equipArmor(armor_A); // protection of 90
        assertEquals(100, hero_A.getRealProtection());
    }

    @Test
    void testGetRealProtection_NoArmor_ShouldNotAddArmorProtection() {
        // Geen armor ingesteld
        assertEquals(10, hero_A.getRealProtection());
    }

    /********************************************************************
     *                          STRENGTH TEST
     ********************************************************************/

    @Test
    void testMultiplyStrength_ShouldMultiplyStrength() {
        hero_A.multiplyStrength(3);
        assertEquals(60, hero_A.getIntrinsicStrength());

        hero_A.multiplyStrength(-2);
        assertEquals(-120, hero_A.getIntrinsicStrength());
    }

    @Test
    void testMultiplyStrength_WithZero_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            hero_A.multiplyStrength(0);
        });
    }

    @Test
    void testDivideStrength_ShouldDivideStrength() {
        hero_A.divideStrength(4);
        assertEquals(5, hero_A.getIntrinsicStrength());

        hero_A.divideStrength(-2);
        assertEquals(-2.5, hero_A.getIntrinsicStrength());
    }

    @Test
    void testDivideStrength_WithZero_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            hero_A.divideStrength(0);
        });
    }

    @Test
    void testeGetAttackPower_WithoutWeapons_ShouldNotAddDamage() {
        // geen wapen
        assertEquals(20, hero_A.getAttackPower());
    }

    @Test
    void testAttackPower_WithWeapons_ShouldAddDamage() {
        hero_A.equipLeftHand(weapon_A);
        hero_A.equipRightHand(weapon_B);
        // 20 + 98 + 98
        assertEquals(216, hero_A.getAttackPower());
    }

    @Test
    void testGetIntrinsicStrength_ReturnValue() {
        assertEquals(20.0, hero_A.getIntrinsicStrength());
    }


    /********************************************************************
     *                       ARMOR TEST
     ********************************************************************/

    @Test
    void testGetArmor_NoArmor_ShouldReturnNull() {
        assertNull(hero_A.getArmor());
    }

    @Test
    void testGetAndEquipArmor_ArmorEquipped_ShouldReturnEquippedArmor() {
        hero_A.equipArmor(armor_A);
        assertEquals(armor_A, hero_A.getArmor());
    }

    @Test
    void testGetNbArmorsCarried_ShouldReturnAmountOfArmors() {
        armor_A.setOwner(hero_A);
        armor_B.setOwner(hero_A);
        assertEquals(2, hero_A.getNbArmorsCarried());
    }

    /********************************************************************
     *                       HIT TEST
     ********************************************************************/

    @Test
    void testHit_SuccessfulNonFatal_ShouldReduceHitPointsMonster() {
        monster_A.setCurrentProtection(0); // zodat hit slaagt

        hero_A.hit(monster_A); // hero does 5 damage

        assertEquals(61, monster_A.getHitPoints());
    }




    @Test
    void testHit_SuccessfulFatal_TriggersHeal() {
        hero_A.removeHitPoints(50); // zodat healing zichtbaar is

        hero_A.equipLeftHand(weapon_A);
        hero_A.equipRightHand(weapon_B);

        monster_A.setCurrentProtection(0); // zodat hit slaagt

        int hpBefore = hero_A.getHitPoints();

        hero_A.hit(monster_A); // hero does 103 damage, so hit is fatal

        assertTrue(hero_A.getHitPoints() > hpBefore); // genezen na kill
        assertEquals(0, monster_A.getHitPoints()); // monster is dood
    }

    @Test
    void testHitFailsIfProtectionTooHigh() {
        Hero hero = new Hero("TestHero", 100, 10);
        Monster monster = new Monster("Broer", 100, 49, new ArrayList<Equipment>(), SkinType.SCALY);
        monster.setCurrentProtection(30);

        int initialHP = monster.getHitPoints();
        hero.hit(monster);

        assertEquals(initialHP, monster.getHitPoints());
        assertFalse(hero.isFighting());
    }

    @Test
    void testHitWithNullMonsterThrows() {
        Hero hero = new Hero("Hero", 100, 10);

        assertThrows(NullPointerException.class, () -> {
            hero.hit(null);
        });
    }


    /********************************************************************
     *                       HEAL TEST
     ********************************************************************/

    @Test
    void testHealAfterKillIsApplied() {
        Hero hero = new Hero("Panda", 100, 20);
        hero.setHitPoints(70); // mist 30 HP

        // Damage moet groot genoeg zijn om zeker te doden
        Weapon sword = new Weapon(21, 21); // veel damage
        hero.getAnchorPoint("leftHand").setItem(sword);

        // Geef monster minder HP zodat het gegarandeerd doodgaat
        Monster monster = new Monster("Outside", 5, 49, new ArrayList<>(), SkinType.SCALY);
        monster.setCurrentProtection(0); // geen verdediging

        hero.hit(monster);

        assertEquals(0, monster.getHitPoints(), "Monster should be dead");

        int healed = hero.getHitPoints() - 70;
        assertTrue(healed > 0 && healed <= 30, "Hero should heal between 1 and 30 HP after a kill");
    }


    /********************************************************************
     *                       COLLECT TREASURE TEST
     ********************************************************************/

    @Test
    void testCollectTreasureFromNullMonsterDoesNothing() {
        Hero hero = new Hero("Lootless", 50, 5.0);
        int capacityBefore = hero.getCapacity();

        // Should not throw and should not alter state
        assertDoesNotThrow(() -> hero.collectTreasureFrom(null));
        assertEquals(capacityBefore, hero.getCapacity(), "Capacity should remain unchanged");
    }

    @Test
    void testCollectSingleWeaponSuccessfully() {
        Hero hero = new Hero("Lara", 100, 5.0); // max capacity = 100

        Weapon loot = new Weapon(24, 49);

        Monster monster = new Monster("Goblin", 30, 49, new ArrayList<Equipment>(), SkinType.SCALY);

        // Zorg dat het op een compatibel anchor zit (bv. "leftHand")
        monster.getAnchors().put("leftHand", loot);

        hero.collectTreasureFrom(monster);

        assertEquals(10, hero.getCapacity(), "Hero's capacity should match item weight");
        assertEquals(hero, loot.getOwner(), "Item owner should be set to hero");
        assertTrue(hero.getAnchors().containsValue(loot), "Item should be in one of hero's anchor points");
    }



    @Test
    void testTooHeavyItemIsNotCollected() {
        Hero hero = new Hero("Tiny", 10, 1.0); // max capacity = 20

        Weapon heavyLoot = new Weapon(30, 14); // too heavy
        Monster monster = new Monster("Ogre", 50, 49, new ArrayList<Equipment>(), SkinType.SCALY);

        for (Map.Entry<String, Equipment> entry : monster.getAnchors().entrySet()) {
            if (entry.getValue() == null) {
                monster.getAnchors().put(entry.getKey(), heavyLoot);
                break;
            }
        }

        hero.collectTreasureFrom(monster);

        assertEquals(0, hero.getCapacity(), "Item too heavy, capacity should stay 0");
        assertNull(heavyLoot.getOwner(), "Owner should remain null");
    }


    @Test
    void testNoValidAnchorAvailable() {
        Hero hero = new Hero("FullBoy", 100, 5.0); // max capacity = 100

        // Vul alle anchor points met dummy items
        for (Equipment item : hero.getAnchors().values()) {
            AnchorPoint dummyAnchor = new AnchorPoint("dummy");
            dummyAnchor.setItem(item); // dit is niet juist - je hebt geen toegang tot de AnchorPoints zelf
        }


        // Monster zonder items
        Monster monster = new Monster("Gremlin", 30, 49, new ArrayList<Equipment>(), SkinType.SCALY);

        // Voeg handmatig een loot-item toe aan een monster anchor
        Weapon loot = new Weapon(5, 14);
        Map<String, Equipment> monsterAnchors = monster.getAnchors();
        // Zoek lege anchor bij monster en stop daar het item in
        for (String key : monsterAnchors.keySet()) {
            if (monsterAnchors.get(key) == null) {
                monsterAnchors.put(key, loot);
                break;
            }
        }

        hero.collectTreasureFrom(monster);

        assertEquals(0, hero.getCapacity(), "Item should not be collected due to no space");
        assertNull(loot.getOwner(), "Item should not be owned");
    }

    /********************************************************************
     *                        ITEM TEST
     ********************************************************************/

    @Test
    void testCanHaveAsItemAt_ValidWeaponInLeftHand() {
        Hero hero = new Hero("Guillaume", 100, 10);

        Weapon weapon = new Weapon(14, 7);
        AnchorPoint leftHand = hero.getAnchorPoint("leftHand");
        leftHand.setItem(null);
        assertTrue(hero.canHaveAsItemAt(weapon, leftHand));
    }

    @Test
    void testCanHaveAsItemAt_ArmorOnBody() {
        Hero hero = new Hero("Guillaume", 100, 10);
        Armor armor = new Armor(20, 5, ArmorType.BRONZE);
        AnchorPoint body = hero.getAnchorPoint("body");
        body.setItem(null);
        assertTrue(hero.canHaveAsItemAt(armor, body));
    }

    @Test
    void testCanHaveAsItemAt_AnyItemOnBack() {
        Hero hero = new Hero("Guillaume", 100, 10);
        Weapon weapon = new Weapon(14, 7);
        AnchorPoint back = hero.getAnchorPoint("back");
        back.setItem(null);
        assertTrue(hero.canHaveAsItemAt(weapon, back));
    }

    @Test
    void testCanHaveAsItemAt_InvalidAnchor() {
        Hero hero = new Hero("Guillaume", 100, 10);
        AnchorPoint unknown = new AnchorPoint("elbow");
        Weapon weapon = new Weapon(14, 7);
        assertFalse(hero.canHaveAsItemAt(weapon, unknown));
    }

    @Test
    void testCanHaveAsItemAt_NullParameters() {
        Hero hero = new Hero("Guillaume", 100, 10);
        assertFalse(hero.canHaveAsItemAt(null, hero.getAnchorPoint("back")));
        assertFalse(hero.canHaveAsItemAt(new Weapon(14, 7), null));
    }

    @Test
    void testCanCarry_TrueWhenUnderLimit() {
        Hero hero = new Hero("Guillaume", 100, 10);
        Weapon weapon = new Weapon(14, 7);
        assertTrue(hero.canCarry(weapon));
    }

    @Test
    void testCanCarry_FalseWhenTooHeavy() {
        Hero hero = new Hero("Guillaume", 100, 10);
        Weapon weapon = new Weapon(400, 7); // Over max capacity
        assertFalse(hero.canCarry(weapon));
    }

    @Test
    void testAddAsItem_Success() {
        Hero hero = new Hero("Guillaume", 100, 10);
        Weapon weapon = new Weapon(14, 7);
        weapon.setOwner(hero);
        assertTrue(hero.hasAsItem(weapon));
    }

    @Test
    void testAddAsItem_AlreadyAdded_Throws() {
        Hero hero = new Hero("Guillaume", 100, 10);
        Weapon weapon = new Weapon(14, 7);
        weapon.setOwner(hero);
        assertThrows(IllegalArgumentException.class, () -> hero.addAsItem(weapon));
    }


    @Test
    void testAddAsItem_TooManyArmors_Throws() {
        Hero hero = new Hero("Guillaume", 100, 10);

        Armor a1 = new Armor(20, 1, ArmorType.BRONZE);
        Armor a2 = new Armor(20, 1, ArmorType.BRONZE);
        Armor a3 = new Armor(20, 1, ArmorType.BRONZE);

        a1.setOwner(hero); // oké
        a2.setOwner(hero); // oké

        // De 3e armor moet falen: te veel harnassen
        assertThrows(IllegalArgumentException.class, () -> a3.setOwner(hero));
    }



    @Test
    void testRemoveAsItem_Success() {
        Hero hero = new Hero("Guillaume", 100, 10);
        Weapon weapon = new Weapon(14, 7);
        weapon.setOwner(hero);
        hero.removeAsItem(weapon);
        assertFalse(hero.hasAsItem(weapon));
    }

    @Test
    void testRemoveAsItem_NotOwned_Throws() {
        Hero hero = new Hero("Guillaume", 100, 10);
        Hero otherHero = new Hero("Alt", 100, 10);
        Weapon weapon = new Weapon(14, 7);

        weapon.setOwner(otherHero); // Geef een andere eigenaar

        assertThrows(IllegalArgumentException.class, () -> hero.removeAsItem(weapon));
    }


}

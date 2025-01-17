/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.dp.primat.domain;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * @author EniSh
 */
public class StudentGroupTest {

    public StudentGroupTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testCreationGroupByFullCode() {
        StudentGroup instance = new StudentGroup("ПК-09-1");

        assertEquals("ПК", instance.getCode());
        assertEquals(Long.valueOf(2009), instance.getYear());
        assertEquals(Long.valueOf(1), instance.getNumber());
        assertEquals("", instance.getGroupType());
    }

    @Test
    public void testCreationGroupByFullCode1() {
        StudentGroup instance = new StudentGroup("ПЗ-12у");

        assertEquals("ПЗ", instance.getCode());
        assertEquals(Long.valueOf(2012), instance.getYear());
        assertEquals(Long.valueOf(1), instance.getNumber());
        assertEquals("у", instance.getGroupType());
    }

    @Test
    public void testCreationGroupByFullCode2() {
        StudentGroup instance = new StudentGroup("ПМ-12с-2");

        assertEquals("ПМ", instance.getCode());
        assertEquals(Long.valueOf(2012), instance.getYear());
        assertEquals(Long.valueOf(2), instance.getNumber());
        assertEquals("с", instance.getGroupType());
    }

    @Test
    public void testGetSemester() throws Exception {
        StudentGroup group = new StudentGroup("ПЗ-12-1");
        Calendar calendar = Calendar.getInstance();
        group.getSemesterForDate(calendar);
        System.out.println(group.getSemesterForDate(calendar));
    }
}
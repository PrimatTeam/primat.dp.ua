package ua.dp.primat;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.dp.primat.curriculum.data.StudentGroup;
import ua.dp.primat.curriculum.planparser.CurriculumParser;
import ua.dp.primat.curriculum.planparser.CurriculumXLSRow;
import static org.junit.Assert.*;

/**
 *
 * @author Acer
 */
public class TestPOI {

    public TestPOI() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testIt() {
        int semesters = 8;
        StudentGroup pz081 = new StudentGroup("PZ", new Long(1), new Long(2008));
        CurriculumParser cParser = new CurriculumParser(pz081, 0, 8, 83, semesters,
                "src/test/resources/PZ_B.07_08_140307_lev4.xls");
        List<CurriculumXLSRow> listParsed = cParser.parse();
        for (int i=0;i<listParsed.size();i++) {
            System.out.println("Discipline: "+listParsed.get(i).getDiscipline().getName());
            System.out.println("Category: "+listParsed.get(i).getWorkload().getLoadCategory());
            System.out.println("Type: "+listParsed.get(i).getWorkload().getType());
            for (int j=0;j<listParsed.get(i).getWorkload().getEntries().size();j++) {
                System.out.println("-> Semester:"+listParsed.get(i).getWorkload().getEntries().get(j).getSemesterNumber()
                        + "| FinalControl:" + listParsed.get(i).getWorkload().getEntries().get(j).getFinalControl()
                        + "| CourseWork:" + listParsed.get(i).getWorkload().getEntries().get(j).getCourceWork()
                        + "| IndividualControlCount:" + listParsed.get(i).getWorkload().getEntries().get(j).getIndividualControl().size());
                for (int k=0;k<listParsed.get(i).getWorkload().getEntries().get(j).getIndividualControl().size();k++) {
                    System.out.print("$ " + listParsed.get(i).getWorkload().getEntries().get(j).getIndividualControl().get(k).getType());
                    System.out.print(", " + listParsed.get(i).getWorkload().getEntries().get(j).getIndividualControl().get(k).getWeekNum());                    
                }
                System.out.print("\n");
            }
            System.out.print("\n");
        }

        //check result
        assertEquals(String.format("We get extacly %d entries",listParsed.size()),true,listParsed.size() > 50);
    }

}
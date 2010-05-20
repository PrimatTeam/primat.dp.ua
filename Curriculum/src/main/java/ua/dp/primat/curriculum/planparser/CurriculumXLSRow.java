package ua.dp.primat.curriculum.planparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import ua.dp.primat.curriculum.data.FinalControlType;
import ua.dp.primat.curriculum.data.LoadCategory;
import ua.dp.primat.curriculum.data.WorkloadType;
import ua.dp.primat.curriculum.data.Cathedra;
import ua.dp.primat.curriculum.data.Discipline;
import ua.dp.primat.curriculum.data.Workload;
import ua.dp.primat.curriculum.data.WorkloadEntry;
import ua.dp.primat.curriculum.data.IndividualControl;
import ua.dp.primat.curriculum.data.StudentGroup;

/**
 *
 * @author fdevelop
 */
public final class CurriculumXLSRow {

    private static final String DIFFERENTIAL_TEST_MARK = "д";

    /**
     * Parses a string like '1,2  ,3, 7', which is used in Curriculum. There is a
     * special case for Setoffs: it could be as '1, 2, 3d, 4', where 'd' means
     * differential setoff (this char is defined by internal DIFFERENTIAL_TEST_MARK constant).
     * @param fmStr - the input string
     * @param standard - if true, a test of differential setoff will be skipped
     * @return The generated array of parsed integers. If standard was false, the result
     *      will include only marked numbers (like '3d').
     */
    private int[] parseNumValues(String fmStr, boolean standard) {
        List<Integer> intValues = new ArrayList<Integer>();
        String[] values = fmStr.split(",");

        for (int i=0;i<values.length;i++) {
            if (standard) {
                try {
                    intValues.add((int)Double.parseDouble(values[i].trim()));
                }
                catch (NumberFormatException nfe) { }
            } else {
                if (values[i].indexOf(DIFFERENTIAL_TEST_MARK) > -1) {
                    String valueWithoutMark = values[i].replaceAll(DIFFERENTIAL_TEST_MARK, "").trim();
                    try {
                        intValues.add((int)Double.parseDouble(valueWithoutMark.trim()));
                    }
                    catch (NumberFormatException nfe) { }
                }
            }
        }

        int[] result = new int[intValues.size()];
        for (int i=0;i<intValues.size();i++) {
            result[i] = intValues.get(i);
        }
        return result;
    }

    /**
     * Parses the individual control strings like 'AO, 2mw' and returns
     * an array of token like ['AO','mw','mw'].
     * @param individualControlTypeCell - the input string
     * @return The string array of tokens
     */
    private String[] parseIndividualControlTypes(String individualControlTypeCell) {
        List<String> listTokens = new ArrayList<String>();
        String workIndForm = individualControlTypeCell.trim();
        while (workIndForm.indexOf(',') > -1) {
            String tokenType = workIndForm.substring(0, workIndForm.indexOf(',')).trim();
            if (tokenType.isEmpty()) continue;
            if ((tokenType.charAt(0) >= '0') && (tokenType.charAt(0) <= '9')) {
                int nextWorksCount = Integer.parseInt(tokenType.substring(0,1));
                for (int y=0;y<nextWorksCount;y++) {
                    listTokens.add(tokenType.substring(1));
                }
            } else
                listTokens.add(tokenType);
            workIndForm = workIndForm.substring(workIndForm.indexOf(',')+1);
        }
        String tokenType = workIndForm.trim();
        if (!tokenType.isEmpty()) {
            if ((tokenType.charAt(0) >= '0') && (tokenType.charAt(0) <= '9')) {
                int nextWorksCount = Integer.parseInt(tokenType.substring(0,1));
                for (int y=0;y<nextWorksCount;y++) {
                    listTokens.add(tokenType.substring(1));
                }
            } else
                listTokens.add(tokenType);
        }

        return listTokens.toArray(new String[0]);
    }

    /**
     * Parses 3 columns for individual controls works info. It includes semester number,
     * type of control work and its week number of year. If the count of tokens
     * in parameters are different, method returns null.
     *
     * @param siwSemester - cell text for semester number of work
     * @param siwForm - cell text for type of control work
     * @param siwWeek - cell text for number of week
     * @return The array of IndividualControlEntry objects or null, if count of tokens
     *      are different.
     */
    private List<IndividualControlEntry> createIndividualWorkList(String siwSemester, String siwForm, String siwWeek) {
        int[] semesters = parseNumValues(siwSemester,true);
        String[] types = parseIndividualControlTypes(siwForm);
        int[] weeks = parseNumValues(siwWeek,true);

        if ((semesters.length != types.length) || (semesters.length != weeks.length)) {
            return null;
        }

        List<IndividualControlEntry> entries = new ArrayList<IndividualControlEntry>();
        for (int i=0;i<semesters.length;i++)
        {
            IndividualControlEntry ic = new IndividualControlEntry();
            ic.setSemester(semesters[i]);
            ic.setType(types[i]);
            ic.setWeekNum(weeks[i]);
            entries.add(ic);
        }

        return entries;
    }

    /**
     * Creates new entity Cathedra
     */
    private void generateCathedra() {
        cathedra = new Cathedra();
        cathedra.setName(cathedraName);
    }

    /**
     * Creates new entity Discipline for specified Cathedra
     * @param cathedra
     */
    private void generateDiscipline(Cathedra cathedra) {
        discipline = new Discipline();
        discipline.setName(disciplineName);
        discipline.setCathedra(cathedra);
    }

    /**
     * Creates new entity Workload for specified Discipline
     * @param discipline
     */
    private void generateWorkload(Discipline discipline) {
        workload = new Workload();
        workload.setDiscipline(discipline);
        workload.setLoadCategory(loadCategory);
        workload.setType(workloadType);
        workload.getGroups().add(group);
    }

    /**
     * Generates list of IndividualControls for specified WorkloadEntry
     * @param workloadEntry
     */
    private void generateIndividualControls(WorkloadEntry workloadEntry) {
        for (IndividualControlEntry ice : indWorks) {
            if (workloadEntry.getSemesterNumber() == ice.getSemester()) {
                IndividualControl ic = new IndividualControl();
                ic.setType(ice.getType());
                ic.setWeekNum(new Long(ice.getWeekNum()));
                workloadEntry.getIndividualControl().add(ic);
            }
        }
    }

    /**
     * Generates list of WorkloadEntry for specified Workload
     * @param workload
     */
    private void generateWorkloadEntries(Workload workload) {
        for (Integer i : hoursForSemesters.keySet()) {
            if (hoursForSemesters.get(i) != null) {
                WorkloadEntry workloadEntry = new WorkloadEntry();
                workloadEntry.setCourceWork(getCourseInSemester(i));
                workloadEntry.setFinalControl(getFinalControlTypeInSemester(i));
                workloadEntry.setIndCount(new Long(Math.round(hoursForSemesters.get(i).getHoursSam()
                        + hoursForSemesters.get(i).getHoursInd())));
                workloadEntry.setLabCount(new Long(Math.round(hoursForSemesters.get(i).getHoursLab())));
                workloadEntry.setLectionCount(new Long(Math.round(hoursForSemesters.get(i).getHoursLec())));
                workloadEntry.setPracticeCount(new Long(Math.round(hoursForSemesters.get(i).getHoursPract())));
                workloadEntry.setSemesterNumber(new Long(i));
                workload.getEntries().add(workloadEntry);
                //individual controls
                generateIndividualControls(workloadEntry);
            }
        }
    }

    /**
     * Executes the generation methods for database objects.
     */
    private void generateDatabaseEntries() {
        generateCathedra();
        generateDiscipline(cathedra);
        generateWorkload(discipline);
        generateWorkloadEntries(workload);
    }

    /**
     * Constructor, that gets atomic info from one row and creates data entities
     * objects (Cathedra, Discipline, Workload)
     * @param group
     * @param disciplineName
     * @param cathedraName
     * @param sfmExams
     * @param sfmTests
     * @param sfmCourses
     * @param siwSemester
     * @param siwForm
     * @param siwWeek
     * @param semesterHours
     * @param workloadType
     * @param loadCategory
     */
    public CurriculumXLSRow(StudentGroup group, String disciplineName, String cathedraName,
                            String sfmExams, String sfmTests, String sfmCourses,
                            String siwSemester, String siwForm, String siwWeek,
                            Map<Integer, WorkHours> semesterHours,
                            WorkloadType workloadType, LoadCategory loadCategory) {
        this.disciplineName = disciplineName;
        this.cathedraName = cathedraName;
        this.workloadType = workloadType;
        this.loadCategory = loadCategory;

        this.fmExams = parseNumValues(sfmExams, true);
        Arrays.sort(fmExams);
        this.fmTests = parseNumValues(sfmTests, true);
        Arrays.sort(fmTests);
        this.fmCourses = parseNumValues(sfmCourses, true);
        Arrays.sort(fmCourses);
        this.fmDifTests = parseNumValues(sfmTests, false);
        Arrays.sort(fmDifTests);

        this.indWorks = createIndividualWorkList(siwSemester, siwForm, siwWeek);
        this.hoursForSemesters = semesterHours;

        this.group = group;

        //generate database entries
        generateDatabaseEntries();
    }

    //Variables
    private String disciplineName;
    private String cathedraName;
    private WorkloadType workloadType;
    private LoadCategory loadCategory;
    //final monitoring
    private int[] fmExams;
    private int[] fmDifTests;
    private int[] fmTests;
    private int[] fmCourses;
    //individual works
    private List<IndividualControlEntry> indWorks;
    //hours info
    private Map<Integer, WorkHours> hoursForSemesters;

    //DataBase output objects
    private StudentGroup group;
    private Cathedra cathedra;
    private Discipline discipline;
    private Workload workload;

    /* getters and setters */

    /**
     * Method, that returns an information about planned course work for specified semester.
     * @param semester
     * @return true - if there is a course work in specified semester
     */
    private boolean getCourseInSemester(int semester) {
        return (Arrays.binarySearch(fmCourses, semester) != -1);
    }

    /**
     * Method, that returns an information about final control type for specified semester.
     * @param semester
     * @return Value of FinalControlType, which indicates a type of final control for specified semester
     */
    private FinalControlType getFinalControlTypeInSemester(int semester) {
        if (Arrays.binarySearch(fmExams, semester) > -1) {
            return FinalControlType.Exam;
        } else if (Arrays.binarySearch(fmTests, semester) > -1) {
            return FinalControlType.Setoff;
        } else if (Arrays.binarySearch(fmDifTests, semester) > -1) {
            return FinalControlType.DifferentiableSetoff;
        } else {
            return FinalControlType.Nothing;
        }
    }

    public Cathedra getCathedra() {
        return cathedra;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public Workload getWorkload() {
        return workload;
    }

}
package ua.dp.primat.repositories;

import java.util.List;

import ua.dp.primat.domain.Cathedra;
import ua.dp.primat.domain.StudentGroup;
import ua.dp.primat.domain.workload.Discipline;

/**
 *
 * @author pesua
 */
public interface DisciplineRepository {

    void delete(Discipline discipline);

    @SuppressWarnings(value = "unchecked")
    List<Discipline> getDisciplines();
    List<Discipline> getDisciplinesForGroupAndSemester(StudentGroup group, long semesterNumber);

    void store(Discipline discipline);
    Discipline update(Discipline discipline);
    List<String> getDisciplineNamesLike(String pattern);
    Discipline findByName(String name);
    Discipline findByNameAndCathedra(String name, Cathedra cathedra);
}


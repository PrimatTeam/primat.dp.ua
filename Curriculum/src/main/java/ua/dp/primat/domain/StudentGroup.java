package ua.dp.primat.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@NamedQueries({
        @NamedQuery(name = StudentGroup.GET_GROUPS_QUERY, query = "select n from StudentGroup n order by n.code, n.year, n.number"),
        @NamedQuery(name = StudentGroup.GET_GROUPS_BY_CODE_AND_YEAR_AND_NUMBER_QUERY, query = "select n from StudentGroup n where n.code = :code and n.year = :year and n.number = :number")
})
public class StudentGroup implements Serializable {

    public static final String GET_GROUPS_QUERY = "getGroups";
    public static final String GET_GROUPS_BY_CODE_AND_YEAR_AND_NUMBER_QUERY = "getGroupsByCodeAndYearAndNumber";
    public static final int CODE_LENGTH = 2;

    private static final Pattern CODE_PATTERN = Pattern.compile("^(.+?)-");
    private static final Pattern YEAR_PATTERN = Pattern.compile("-(\\d{2})");
    private static final Pattern TYPE_PATTERN = Pattern.compile("\\d{2}([а-яa-z]?)");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-(\\d)$");

    public StudentGroup() {
    }

    public StudentGroup(String code, Long number, Long year, String groupType) {
        this(code, number, year);
        this.groupType = groupType;
    }

    public StudentGroup(String code, Long number, Long year) {
        this.code = code;
        this.number = number;
        this.year = year;
    }

    public StudentGroup(String fullCode) {
        if (!Pattern.matches("\\D{2}\\-\\d{2}[а-я\\w]?(-\\d)?", fullCode)) {
            throw new IllegalArgumentException("Wrong student group code");
        }

        Matcher codeMatcher = CODE_PATTERN.matcher(fullCode);
        codeMatcher.find();
        code = codeMatcher.group(1);

        Matcher yearMatcher = YEAR_PATTERN.matcher(fullCode);
        yearMatcher.find();
        year = YEARBASE + Long.valueOf(yearMatcher.group(1));

        Matcher typeMatcher = TYPE_PATTERN.matcher(fullCode);
        typeMatcher.find();
        groupType = typeMatcher.group(1);

        Matcher numberMatcher = NUMBER_PATTERN.matcher(fullCode);
        if (numberMatcher.find()) {
            number = Long.valueOf(numberMatcher.group(1));
        } else {
            number = 1L;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long groupId) {
        this.id = groupId;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StudentGroup other = (StudentGroup) obj;
        return new EqualsBuilder()
                .append(code, other.code)
                .append(year, other.year)
                .append(number, other.number)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(code)
                .append(year)
                .append(number)
                .hashCode();
    }

    @Override
    public String toString() {
        final int yearMask = 100;
        final DecimalFormat format = new DecimalFormat("00");
        final String yearCode = format.format(getYear() % yearMask);
        return String.format("%s-%s-%d", getCode(), yearCode, getNumber());
    }

    private static final Long YEARBASE = 2000L;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = CODE_LENGTH)
    private String code;
    private Long number;
    private Long year;
    private String groupType;
}

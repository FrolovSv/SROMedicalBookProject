package com.Class;

import com.Class.MedicalBook.EnumBorneDiseases;
import com.SQL.SQLConnection;
import com.SQL.SQLQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MedicalBook extends MainClassProgect<MedicalBook> {

    private Integer NumMedicalBook = -1;
    private Integer EmployeeId = -1;

    private String EmployeeName = "";
    private String EmployeeLastName = "";
    private String EmployeePatronymic = "";
    private String EmployeeUsGroupName = "";
    private String EmployeeUsDepartmentName = "";
    private String EmployeeUsPositionName = "";
    private Date EmployeeBerthday = new Date(0);
    private Integer EmployeeInstitutionId = -1;

    private String MainEmployeeName = "";
    private String MainEmployeeLastName = "";
    private String MainEmployeePatronymic = "";
    private String MainEmployeeUsGroupName = "";
    private String MainEmployeeUsDepartmentName = "";
    private String MainEmployeeUsPositionName = "";

    private Integer MainEmployeeId = 0;
    private Boolean SerchLineStaff = false;
    private Boolean SerchWithoutEmployees = false;

    private ArrayList<EnumBorneDiseases> BorneDiseases = new ArrayList<>();//перенесенные заболевания
    private Date EmploymentDate = new Date(0);//дата приема на работу
    private Date Therapist = new Date(0);//терапевт
    private Date Otolaryngologist = new Date(0);//ЛОР
    private Date Dentist = new Date(0);//Стоматолог
    private Date ExpertInNarcology = new Date(0);//нарколог
    private Date Psychiatrist = new Date(0); //психиатор
    private Date Dermatovenerologist = new Date(0); //дерматовенеролог
    private Date Fluorography = new Date(0); //флюорография
    private Date Helminths = new Date(0); //Гельминты
    private Date IntestinalInfection = new Date(0); //Кишечная инфекция
    private Date TyphoidFever = new Date(0); // Брюшной тиф
    private Date Staphylococcus = new Date(0); //стаффилокок
    private Date Validation = new Date(0); //Аттестация
    private Date Shigellvak = new Date(0); //Шигелвак
    private Date Hepatitis_A = new Date(0); //Геппатит А
    private Date Hepatitis_A2 = new Date(0); //Геппатит А
    private Date Diphtheria = new Date(0); //АДСМ
    private Date Measles = new Date(0); //корь
    private Date Measles_2 = new Date(0); //корь
    private Date Hepatitis_B = new Date(0); //Геппатит Б
    private Date Hepatitis_B2 = new Date(0); //Геппатит Б
    private Date Hepatitis_B3 = new Date(0); //Геппатит Б
    private boolean ValidationForYear = true; //годовалая аттестация
    private Date Rubella = new Date(0); //краснуха
    private ArrayList<EnumHepatitisAvaccine> HepatitisAvaccine = new ArrayList<>(); //вакцина геппатита А
    private String Note = "";
    private Integer RegionId = 0;
    private boolean IsDeleted = false;
    private String InstitutionName = "";
    private Integer InstitutionId = -1;
    private Integer TypeUnitId = -1;
    private String TextSearch = "";

    public String getInstitutionName() {
        return InstitutionName == null ? "" : InstitutionName;
    }

    public String getEmployeeName() {
        return EmployeeName == null ? "" : EmployeeName;
    }

    public String getEmployeeLastName() {
        return EmployeeLastName == null ? "" : EmployeeLastName;
    }

    public String getEmployeePatronymic() {
        return EmployeePatronymic == null ? "" : EmployeePatronymic;
    }

    public String getEmployeeUsGroupName() {
        return EmployeeUsGroupName == null ? "" : EmployeeUsGroupName;
    }

    public String getEmployeeUsDepartmentName() {
        return EmployeeUsDepartmentName == null ? "" : EmployeeUsDepartmentName;
    }

    public String getEmployeeUsPositionName() {
        return EmployeeUsPositionName == null ? "" : EmployeeUsPositionName;
    }

    public String getMainEmployeeName() {
        return MainEmployeeName == null ? "" : MainEmployeeName;
    }

    public String getMainEmployeeLastName() {
        return MainEmployeeLastName == null ? "" : MainEmployeeLastName;
    }

    public String getMainEmployeePatronymic() {
        return MainEmployeePatronymic == null ? "" : MainEmployeePatronymic;
    }

    public String getMainEmployeeUsGroupName() {
        return MainEmployeeUsGroupName == null ? "" : MainEmployeeUsGroupName;
    }

    public String getMainEmployeeUsDepartmentName() {
        return MainEmployeeUsDepartmentName == null ? "" : MainEmployeeUsDepartmentName;
    }

    public String getMainEmployeeUsPositionName() {
        return MainEmployeeUsPositionName == null ? "" : MainEmployeeUsPositionName;
    }

    public Integer getEmployeeId() {
        return EmployeeId == null ? 0 : EmployeeId;
    }

    public MedicalBook() {
    }

    @Override
    public Object[] getDataForTable() {
        Object[] DataForTable = new Object[11];
//          № п/п	Integer	false	false
//          Номер ЛМК	String	false	false
//          ФИО	String	false	false
//          Год рождения	String	false	false
//          Группа	String	false	false
//          Подразделение	String	false	false
//          Должность	String	false	false
//          Данные руководителя	String	false	false
//          Ближайшее	String	false	false
//          Статус	String	false	false
//          Удаленная	String	false	false		
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sb = new StringBuilder();
        HashMap<String, Boolean> res = checkValidMedicalBook();
        if (this.isIsDeleted()) {
            sb.append("Удаленная");
        } else {
            sb.append("Активная");
        }

        String EmplName = "";
        if (this.getEmployeeId() <= 0) {
            EmplName = "сотрудник не выбран";
        } else {
            EmplName = this.getSurnameInitials(getEmployeeLastName(), getEmployeeName(), getEmployeePatronymic());
        }

        DataForTable[0] = (int) 1;
        DataForTable[1] = this.getNumMedicalBook() <= 0 ? "" : this.getNumMedicalBook();
        DataForTable[2] = EmplName;
        DataForTable[3] = df.format(this.getEmployeeBerthday());
        DataForTable[4] = SubSting(this.getEmployeeUsGroupName(), 20);
        //DataForTable[5] = SubSting(this.getEmployeeUsDepartmentName(),20);
        DataForTable[5] = !getInstitutionName().equals("") ? getInstitutionName() : getEmployeeUsDepartmentName();
        DataForTable[6] = SubSting(this.getEmployeeUsPositionName(), 20);
        DataForTable[7] = getSurnameInitials(getMainEmployeeLastName(), getMainEmployeeName(), getMainEmployeePatronymic());
        // прописать логику подсчета крайнего необходимого мероприятия по ЛМК
        StringBuilder text = new StringBuilder();
        res.entrySet().stream()
                .filter((entry) -> (entry.getValue() == false))
                .forEachOrdered((entry) -> {
                    text.append(entry.getKey()).append(", ");
                });

        DataForTable[8] = text.length() > 2 ? text.toString().substring(0, text.length() - 2) : "";
        // прописать логику определения валидности ЛМК
        DataForTable[9] = !res.containsValue(false) ? "Норма" : "Просрочено";
        DataForTable[10] = sb;
        return DataForTable;
    }

    // ========================== установки sql ============================
    // ==================== геттеры  SELECT ====================
    @Override
    public String getQuerySelect(Integer Limit, Integer NumMedicalBook) {
        String str = "";
        str = "SELECT "
                + "MB.`NumMedicalBook`, MB.`EmployeeId`, MB.`DateAdd`, MB.`EmployeeAddId`, MB.`DateChange`, " //5
                + "MB.`EmployeeChangeID`, MB.`BorneDiseases`, MB.`Therapist`, MB.`Otolaryngologist`, MB.`Dentist`, " //5
                + "MB.`ExpertInNarcology`, MB.`Psychiatrist`, MB.`Dermatovenerologist`, MB.`Fluorography`, MB.`Helminths`, "//5
                + "MB.`IntestinalInfection`, MB.`TyphoidFever`, MB.`Staphylococcus`, MB.`Validation`, MB.`Shigellvak`, "//5
                + "MB.`Hepatitis_A`, MB.`Hepatitis_A2`, MB.`Diphtheria`, MB.`Measles`, MB.`Hepatitis_B`, "//5
                + "MB.`Hepatitis_B2`, MB.`Hepatitis_B3`, MB.`ValidationForYear`, MB.`Note`, MB.`RegionId`, "//5
                + "MB.`Rubella`, MB.`hepatitisAvaccine`, MB.`Measles_2`, MB.`isDeleted`, Employee.EmploymentDate "//4
                + "FROM `MedicalBook` MB "
                + "left join Employee on (MB.EmployeeId = Employee.Id) "
                + (NumMedicalBook > 0 ? "Where MB.`NumMedicalBook` = " + NumMedicalBook + " " : "")
                + "GROUP BY `MB`.`NumMedicalBook` "
                + (Limit > 0 ? "Limit " + Limit.toString() : "");
        return str;
    }

    @Override
    public String getQuerySelectTable(Integer Limit) {
        String str = "SELECT "
                + "MB.NumMedicalBook, MB.EmployeeId, MB.BorneDiseases, MB.Therapist, MB.Otolaryngologist, MB.Dentist, " //6
                + "MB.ExpertInNarcology, MB.Psychiatrist, MB.Dermatovenerologist, MB.Fluorography, MB.Helminths, "//5
                + "MB.IntestinalInfection, MB.TyphoidFever, MB.Staphylococcus, MB.Validation, MB.Shigellvak, "//5
                + "MB.Hepatitis_A, MB.Hepatitis_A2, MB.Diphtheria, MB.Measles, MB.Hepatitis_B, "//5
                + "MB.Hepatitis_B2, MB.Hepatitis_B3, MB.ValidationForYear, MB.Note, MB.RegionId, "//5
                + "MB.Rubella, MB.hepatitisAvaccine, MB.Measles_2, MB.isDeleted, " //4
                + "Emp.LastName, Emp.name, Emp.Patronymic, " //3
                + "UsDepEmp.name, UsGrEmp.name, UsPosEmp.name, " //3
                + "MainEmp.name, MainEmp.LastName, MainEmp.Patronymic, " //3
                + "Inst.Name, Emp.InstitutionId, Emp.Berthday, Emp.EmploymentDate "//2
                + "FROM `MedicalBook` MB "
                + "Left join Employee Emp on (Emp.id = MB.EmployeeId) "
                + "left join Employee MainEmp on (MainEmp.id = Emp.MainEmployeeId) "
                + "Left join UsPosition UsPosEmp on (UsPosEmp.id = Emp.UsPositionId) "
                + "Left join UsDepartment UsDepEmp on (UsDepEmp.id = UsPosEmp.UsDepartmentId) "
                + "Left join UsGroup UsGrEmp on (UsGrEmp.id = UsDepEmp.UsGroupId) "
                + "Left join Institution Inst on (Inst.id = Emp.InstitutionId) "
                + "Where MB.isDeleted = " + (this.isIsDeleted() ? 1 : 0) + " "
                + (this.getSerchWithoutEmployees() ? " and MB.EmployeeId is null " : " and MB.EmployeeId is not null ")
                + "%getSerchLineStaff"
                + "%getTypeUnitId"
                + "%getMainEmployeeId"
                + "%getGroup"
                + "%getDepartment"
                + "%getPosition"
                + "%6where"
                + "%7where"
                + "GROUP BY `MB`.`NumMedicalBook` "
                + (Limit == -1 ? "" : "Limit " + Limit.toString());

        if (this.getSerchWithoutEmployees()) {
            this.setMainEmployeeId(0);
            str = str.replace("%getSerchLineStaff", "");
            str = str.replace("%getTypeUnitId", "");
        } else {
            str = str.replace("%getSerchLineStaff", (this.getSerchLineStaff() ? " and Emp.InstitutionId is not null " : "and Emp.InstitutionId is null"));
            str = str.replace("%getTypeUnitId", (this.getTypeUnitId() > 0 ? " and UsGrEmp.TypeUnitId = " + this.getTypeUnitId() + " " : " "));
        }

        if (this.getMainEmployeeId() > 0) {
            str = str.replace("%getMainEmployeeId", " and MainEmp.`Id` = " + this.getMainEmployeeId() + " ");
            str = str.replace("%getGroup", "");
            str = str.replace("%getDepartment", "");
            str = str.replace("%getPosition", "");
        } else {
            str = str.replace("%getMainEmployeeId", "");
            str = str.replace("%getGroup", (!this.getEmployeeUsGroupName().equals("") ? "and UsGrEmp.name LIKE '%" + this.getEmployeeUsGroupName() + "%' " : ""));
            str = str.replace("%getDepartment", (!this.getEmployeeUsDepartmentName().equals("") ? "and UsDepEmp.name LIKE '%" + this.getEmployeeUsDepartmentName() + "%' " : ""));
            str = str.replace("%getPosition", (!this.getEmployeeUsPositionName().equals("") ? "and UsPosEmp.name LIKE '%" + this.getEmployeeUsPositionName() + "%' " : ""));
            str = str.replace("%getTypeUnitId", (this.getTypeUnitId() > 0 ? " and UsGrEmp.TypeUnitId = " + this.getTypeUnitId() + " " : " "));
        }

        if (this.getTextSearch().equals("")) {
            str = str.replace("%6where", "");
            str = str.replace("%7where", "");
        } else if (!isDigit(this.getTextSearch())) {
            str = str.replace("%6where", " and ("
                    + "   Emp.`LastName` LIKE '%" + this.getTextSearch() + "%' "
                    + "or Emp.`Name` LIKE '%" + this.getTextSearch() + "%' "
                    + "or Emp.`Patronymic` LIKE '%" + this.getTextSearch() + "%' "
                    + "or UsPosEmp.`name` LIKE '%" + this.getTextSearch() + "%') "
                    + (!this.getInstitutionName().equals("") ? this.getInstitutionName() : ""));
            str = str.replace("%7where", " ");
        } else if (isDigit(this.getTextSearch())) {
            str = str.replace("%7where", " and MB.`NumMedicalBook` LIKE '%" + this.getTextSearch() + "%' ");
            str = str.replace("%6where", " ");
        }

        return str;
    }

    @Override // не переопределен
    public String getQueryLastKey() {
        System.out.println("Class.NewMedicalBook.getQueryLastKey()");
        return "Class.NewMedicalBook.getQueryLastKey()";
    }

    @Override
    public String getQueryIsExistKey(Integer Key) {
        String str = "SELECT count(MB.NumMedicalBook) "
                + "FROM `MedicalBook` MB "
                + "WHERE MB.NumMedicalBook = " + Key;
        return str;
    }

    public String getQueryIsExistKey() {
        String str = "SELECT count(MB.NumMedicalBook) "
                + "FROM `MedicalBook` MB "
                + "WHERE MB.NumMedicalBook = " + getNumMedicalBook();
        return str;
    }

    // ==================== геттеры  UPSERT ====================
    @Override
    public String getQueryUpdate(Integer UnitId) {
        String str = "UPDATE "
                + "`MedicalBook` SET "
                + "`NumMedicalBook` = ?, `EmployeeId` = ?, `DateAdd` = ?, `EmployeeAddId` = ?, `DateChange` = ?, "
                + "`EmployeeChangeID` = ?, `BorneDiseases` = ?, `Rubella` = ?, `Therapist` = ?, `Otolaryngologist` = ?, "
                + "`Dentist` = ?, `ExpertInNarcology` = ?, `Psychiatrist` = ?, `Dermatovenerologist` = ?, `Fluorography` = ?, "
                + "`Helminths` = ?, `IntestinalInfection` = ?, `TyphoidFever` = ?, `Staphylococcus` = ?, `Validation` = ?, "
                + "`Shigellvak` = ?, `Hepatitis_A` = ?, `Hepatitis_A2` = ?, `hepatitisAvaccine` = ?, `Diphtheria` = ?, "
                + "`Measles` = ?, `Measles_2` = ?, `Hepatitis_B` = ?, `Hepatitis_B2` = ?, `Hepatitis_B3` = ?, "
                + "`ValidationForYear` = ?, `Note` = ?, `RegionId` = ?, `IsDeleted` = ? "
                + "WHERE MedicalBook.NumMedicalBook = " + UnitId;
        return str;
    }

    @Override
    public String getQueryInsert() {
        String str = "INSERT INTO `MedicalBook`("
                + "`NumMedicalBook`, `EmployeeId`, `DateAdd`, `EmployeeAddId`, `DateChange`, "
                + "`EmployeeChangeID`, `BorneDiseases`, `Rubella`, `Therapist`, `Otolaryngologist`, "
                + "`Dentist`, `ExpertInNarcology`, `Psychiatrist`, `Dermatovenerologist`, `Fluorography`, "
                + "`Helminths`, `IntestinalInfection`, `TyphoidFever`, `Staphylococcus`, `Validation`, "
                + "`Shigellvak`, `Hepatitis_A`, `Hepatitis_A2`, `hepatitisAvaccine`, `Diphtheria`, "
                + "`Measles`, `Measles_2`, `Hepatitis_B`, `Hepatitis_B2`, `Hepatitis_B3`, "
                + "`ValidationForYear`, `Note`, `RegionId`, `IsDeleted` ) "
                + "VALUES "
                + "(?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?,?,"
                + "?,?,?,?)";
        return str;
    }

    // ==================== PreparedStatment для UPSERT запросов ====================
    @Override
    public void setPreparedStatment(PreparedStatement ps) throws SQLException {
//            + "`NumMedicalBook`, `EmployeeId`, `DateAdd`, `EmployeeAddId`, `DateChange`, "
//            + "`EmployeeChangeID`, `BorneDiseases`, `Rubella`, `Therapist`, `Otolaryngologist`, "
//            + "`Dentist`, `ExpertInNarcology`, `Psychiatrist`, `Dermatovenerologist`, `Fluorography`, "
//            + "`Helminths`, `IntestinalInfection`, `TyphoidFever`, `Staphylococcus`, `Validation`, "
//            + "`Shigellvak`, `Hepatitis_A`, `Hepatitis_A2`, `hepatitisAvaccine`, `Diphtheria`, "
//            + "`Measles`, `Measles_2`, `Hepatitis_B`, `Hepatitis_B2`, `Hepatitis_B3`, "
//            + "`ValidationForYear`, `Note`, `RegionId`, `IsDeleted`) "

        ps.setInt(1, this.getNumMedicalBook());
        if (this.getEmployeeId() <= 0) {
            ps.setNull(2, 0);
        } else {
            ps.setInt(2, this.getEmployeeId());
        }
        ps.setDate(3, new java.sql.Date(this.getDateAdd().getTime()));
        ps.setInt(4, this.getEmployeeAddId());
        ps.setDate(5, new java.sql.Date(new Date().getTime()));

        ps.setInt(6, this.getEmployeeChangeId());
        ps.setString(7, enumToString(getBorneDiseases()));
        ps.setDate(8, new java.sql.Date(this.getRubella().getTime()));
        ps.setDate(9, new java.sql.Date(this.getTherapist().getTime()));
        ps.setDate(10, new java.sql.Date(this.getOtolaryngologist().getTime()));

        ps.setDate(11, new java.sql.Date(this.getDentist().getTime()));
        ps.setDate(12, new java.sql.Date(this.getExpertInNarcology().getTime()));
        ps.setDate(13, new java.sql.Date(this.getPsychiatrist().getTime()));
        ps.setDate(14, new java.sql.Date(this.getDermatovenerologist().getTime()));
        ps.setDate(15, new java.sql.Date(this.getFluorography().getTime()));

        ps.setDate(16, new java.sql.Date(this.getHelminths().getTime()));
        ps.setDate(17, new java.sql.Date(this.getIntestinalInfection().getTime()));
        ps.setDate(18, new java.sql.Date(this.getTyphoidFever().getTime()));
        ps.setDate(19, new java.sql.Date(this.getStaphylococcus().getTime()));
        ps.setDate(20, new java.sql.Date(this.getValidation().getTime()));

        ps.setDate(21, new java.sql.Date(this.getShigellvak().getTime()));
        ps.setDate(22, new java.sql.Date(this.getHepatitis_A().getTime()));
        ps.setDate(23, new java.sql.Date(this.getHepatitis_A2().getTime()));
        ps.setString(24, enumToString(getHepatitisAvaccine()));
        ps.setDate(25, new java.sql.Date(this.getDiphtheria().getTime()));

        ps.setDate(26, new java.sql.Date(this.getMeasles().getTime()));
        ps.setDate(27, new java.sql.Date(this.getMeasles_2().getTime()));
        ps.setDate(28, new java.sql.Date(this.getHepatitis_B().getTime()));
        ps.setDate(29, new java.sql.Date(this.getHepatitis_B2().getTime()));
        ps.setDate(30, new java.sql.Date(this.getHepatitis_B3().getTime()));

        ps.setBoolean(31, this.isValidationForYear());
        ps.setString(32, this.getNote());
        ps.setInt(33, this.getRegionId());
        ps.setBoolean(34, this.isIsDeleted());
        //ps.setDate(35, new java.sql.Date( this.getEmploymentDate().getTime()));
    }

    // ==================== ResultSet для SELECT запросов ====================
    @Override
    public MedicalBook getDataFromResultSet(ResultSet rs) throws SQLException {
        MedicalBook MedicalBook = new MedicalBook();

        MedicalBook.setNumMedicalBook(rs.getInt(1));
        MedicalBook.setEmployeeId(rs.getInt(2));
        MedicalBook.setDateAdd(rs.getDate(3));
        MedicalBook.setEmployeeAddId(rs.getInt(4));
        MedicalBook.setDateChange(rs.getDate(5));
        MedicalBook.setEmployeeChangeId(rs.getInt(6));
        MedicalBook.setBorneDiseases(StringToEnumBorneDeseases(rs.getString(7)));
        MedicalBook.setTherapist(rs.getDate(8));
        MedicalBook.setOtolaryngologist(rs.getDate(9));
        MedicalBook.setDentist(rs.getDate(10));
        MedicalBook.setExpertInNarcology(rs.getDate(11));
        MedicalBook.setPsychiatrist(rs.getDate(12));
        MedicalBook.setDermatovenerologist(rs.getDate(13));
        MedicalBook.setFluorography(rs.getDate(14));
        MedicalBook.setHelminths(rs.getDate(15));
        MedicalBook.setIntestinalInfection(rs.getDate(16));
        MedicalBook.setTyphoidFever(rs.getDate(17));
        MedicalBook.setStaphylococcus(rs.getDate(18));
        MedicalBook.setValidation(rs.getDate(19));
        MedicalBook.setShigellvak(rs.getDate(20));
        MedicalBook.setHepatitis_A(rs.getDate(21));
        MedicalBook.setHepatitis_A2(rs.getDate(22));
        MedicalBook.setDiphtheria(rs.getDate(23));
        MedicalBook.setMeasles(rs.getDate(24));
        MedicalBook.setHepatitis_B(rs.getDate(25));
        MedicalBook.setHepatitis_B2(rs.getDate(26));
        MedicalBook.setHepatitis_B3(rs.getDate(27));
        MedicalBook.setValidationForYear(rs.getBoolean(28));
        MedicalBook.setNote(rs.getString(29));
        MedicalBook.setRegionId(rs.getInt(30));
        MedicalBook.setRubella(rs.getDate(31));
        MedicalBook.setHepatitisAvaccine(StringToEnumHepatitisAvaccine(rs.getString(32)));
        MedicalBook.setMeasles_2(rs.getDate(33));
        MedicalBook.setIsDeleted(rs.getBoolean(34));
        MedicalBook.setEmploymentDate(rs.getDate(35));

        //if (rs.getBytes(22)!=null)
        //    User.setPhotoPath(WriteByteArrayToFile(rs.getBytes(22), "User"+rs.getString(1)));
        return MedicalBook;
    }

    @Override
    public MedicalBook getDataFromResultSetTable(ResultSet rs) throws SQLException {
//        + "MB.NumMedicalBook, MB.EmployeeId, MB.BorneDiseases, MB.Therapist, MB.Otolaryngologist, MB.Dentist, " //6
//        + "MB.ExpertInNarcology, MB.Psychiatrist, MB.Dermatovenerologist, MB.Fluorography, MB.Helminths, "//5
//        + "MB.IntestinalInfection, MB.TyphoidFever, MB.Staphylococcus, MB.Validation, MB.Shigellvak, "//5
//        + "MB.Hepatitis_A, MB.Hepatitis_A2, MB.Diphtheria, MB.Measles, MB.Hepatitis_B, "//5
//        + "MB.Hepatitis_B2, MB.Hepatitis_B3, MB.ValidationForYear, MB.Note, MB.RegionId, "//5
//        + "MB.Rubella, MB.hepatitisAvaccine, MB.Measles_2, MB.isDeleted, " //4
//        + "Emp.LastName, Emp.name, Emp.Patronymic, " //3
//        + "UsDepEmp.name, UsGrEmp.name, UsPosEmp.name, " //3
//        + "MainEmp.name, MainEmp.LastName, MainEmp.Patronymic, " //3
//        + "Inst.Name, Emp.InstitutionId "//2

        MedicalBook MedicalBook = new MedicalBook();
        MedicalBook.setNumMedicalBook(rs.getInt(1));
        MedicalBook.setEmployeeId(rs.getInt(2));
        MedicalBook.setBorneDiseases(StringToEnumBorneDeseases(rs.getString(3)));
        MedicalBook.setTherapist(rs.getDate(4));
        MedicalBook.setOtolaryngologist(rs.getDate(5));
        MedicalBook.setDentist(rs.getDate(6));
        MedicalBook.setExpertInNarcology(rs.getDate(7));
        MedicalBook.setPsychiatrist(rs.getDate(8));
        MedicalBook.setDermatovenerologist(rs.getDate(9));
        MedicalBook.setFluorography(rs.getDate(10));
        MedicalBook.setHelminths(rs.getDate(11));
        MedicalBook.setIntestinalInfection(rs.getDate(12));
        MedicalBook.setTyphoidFever(rs.getDate(13));
        MedicalBook.setStaphylococcus(rs.getDate(14));
        MedicalBook.setValidation(rs.getDate(15));
        MedicalBook.setShigellvak(rs.getDate(16));
        MedicalBook.setHepatitis_A(rs.getDate(17));
        MedicalBook.setHepatitis_A2(rs.getDate(18));
        MedicalBook.setDiphtheria(rs.getDate(19));
        MedicalBook.setMeasles(rs.getDate(20));
        MedicalBook.setHepatitis_B(rs.getDate(21));
        MedicalBook.setHepatitis_B2(rs.getDate(22));
        MedicalBook.setHepatitis_B3(rs.getDate(23));
        MedicalBook.setValidationForYear(rs.getBoolean(24));
        MedicalBook.setNote(rs.getString(25));
        MedicalBook.setRegionId(rs.getInt(26));
        MedicalBook.setRubella(rs.getDate(27));
        MedicalBook.setHepatitisAvaccine(StringToEnumHepatitisAvaccine(rs.getString(28)));
        MedicalBook.setMeasles_2(rs.getDate(29));
        MedicalBook.setIsDeleted(rs.getBoolean(30));

        MedicalBook.setEmployeeLastName(rs.getString(31));
        MedicalBook.setEmployeeName(rs.getString(32));
        MedicalBook.setEmployeePatronymic(rs.getString(33));

        MedicalBook.setEmployeeUsDepartmentName(rs.getString(34));
        MedicalBook.setEmployeeUsGroupName(rs.getString(35));
        MedicalBook.setEmployeeUsPositionName(rs.getString(36));

        MedicalBook.setMainEmployeeLastName(rs.getString(37));
        MedicalBook.setMainEmployeeName(rs.getString(38));
        MedicalBook.setMainEmployeePatronymic(rs.getString(39));

        MedicalBook.setInstitutionName(rs.getString(40));
        MedicalBook.setEmployeeInstitutionId(rs.getInt(41));
        MedicalBook.setEmployeeBerthday(rs.getDate(42));
        
        MedicalBook.setEmploymentDate(rs.getDate(43));
        return MedicalBook;
    }

    public enum EnumBorneDiseases {
        Rubella,
        Diphtheria,
        Measles,
        Hepatitis_A,
        Hepatitis_B
    }

    public enum EnumHepatitisAvaccine {
        Algawak(12, 240),
        Avaxim(12, 240),
        Wakta(12, 240);

        int val1;
        int val2;

        EnumHepatitisAvaccine(int val1, int val2) {
            this.val1 = val1;
            this.val2 = val2;
        }

        EnumHepatitisAvaccine() {
            this.val1 = 0;
            this.val2 = 0;
        }
    }

    // ========================== прочее ============================
    @Override
    public MedicalBook clone() {
        try {
            return (MedicalBook) super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println("Class.MedicalBook.clone()" + ex.getMessage());
            return new MedicalBook();
        }
    }

    public boolean isNumMedicalBookExist(SQLConnection SQLConnection, Integer NumMedBook) {
        Boolean isNewMedBook = false;
        Boolean IsNewCon = false;
        if (SQLConnection.getCon() == null || SQLConnection.getStmt() == null) {
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        try {
            String query = "SELECT `NumMedicalBook` FROM MedicalBook WHERE `NumMedicalBook`= " + NumMedBook + " LIMIT 1";
            ResultSet rs = SQLConnection.getStmt().executeQuery(query);
            int Key = -1;
            while (rs.next()) {
                Key = rs.getInt(1);
            }
            if (Key >= 0) {
                isNewMedBook = true;
            }
            rs.close();
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            SQLConnection.closeConnection();
        } finally {
            if (IsNewCon) {
                SQLConnection.closeConnection();
            }
        }
        return isNewMedBook;
    }

    public boolean[] CheckEmployeeId(SQLConnection SQLConnection) {
        boolean IsEnter = false;
        boolean IsDeletede = false;
        boolean IsNewCon = false;
        if (SQLConnection.getCon() == null || SQLConnection.getStmt() == null) {
            SQLConnection.getConnection();
            IsNewCon = true;
        }
        try {
            String query = "SELECT `EmployeeId`, `IsDeleted` FROM MedicalBook WHERE `EmployeeId`= " + this.getEmployeeId() + " LIMIT 1";
            ResultSet rs = SQLConnection.getStmt().executeQuery(query);
            int Key = -1;
            while (rs.next()) {
                Key = rs.getInt(1);
                IsDeletede = rs.getBoolean(2);
            }
            if (Key == 0) {
                IsEnter = true;
            }
            rs.close();
        } catch (SQLException sqlEx) {
            sqlEx.getMessage();
            SQLConnection.closeConnection();
        } finally {
            if (IsNewCon) {
                SQLConnection.closeConnection();
            }
        }
        return new boolean[]{IsEnter, IsDeletede};
    }

    public HashMap<String, Boolean> checkValidMedicalBook() {
        HashMap<String, Boolean> result = new HashMap<>();
        Boolean Valid = false;
        String NameField = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Calendar CurrentDayMinus1Year = new GregorianCalendar();
            CurrentDayMinus1Year.add(Calendar.YEAR, -1);

            Calendar CurrentDayMinus10Year = new GregorianCalendar();
            CurrentDayMinus10Year.add(Calendar.YEAR, -10);

            Calendar CurrentDayMinus25Year = new GregorianCalendar();
            CurrentDayMinus25Year.add(Calendar.YEAR, -25);

            Calendar CurrentDayMinus55Year = new GregorianCalendar();
            CurrentDayMinus55Year.add(Calendar.YEAR, -55);

            Calendar CurrentDayMinus180Day = new GregorianCalendar();
            CurrentDayMinus180Day.add(Calendar.DAY_OF_YEAR, -180);

            Calendar CurrentDayMinus35Day = new GregorianCalendar();
            CurrentDayMinus35Day.add(Calendar.DAY_OF_YEAR, -35);

            Calendar CurrentDayMinus3Month = new GregorianCalendar();
            CurrentDayMinus3Month.add(Calendar.MONTH, -3);

            Calendar CurrentDayMinus5Month = new GregorianCalendar();
            CurrentDayMinus5Month.add(Calendar.MONTH, -5);
            Calendar CurrentDayMinus5Month5Day = new GregorianCalendar();
            CurrentDayMinus5Month5Day.add(Calendar.MONTH, -5);
            CurrentDayMinus5Month5Day.add(Calendar.DAY_OF_YEAR, -5);

            Calendar CurrentDayMinus1Month = new GregorianCalendar();
            CurrentDayMinus1Month.add(Calendar.MONTH, -1);

            String[] splitgetMeasles_2 = sdf.format(getMeasles_2()).split("-");
            Calendar DateMeasles_2Minus3Month = new GregorianCalendar(Integer.valueOf(splitgetMeasles_2[0]), Integer.valueOf(splitgetMeasles_2[1]) - 1, Integer.valueOf(splitgetMeasles_2[2]));
            DateMeasles_2Minus3Month.add(Calendar.MONTH, -3);

            String[] splitgetHepatitis_A2 = sdf.format(getHepatitis_A2()).split("-");
            Calendar DateHepatitis_A2Minus1Year = new GregorianCalendar(Integer.valueOf(splitgetHepatitis_A2[0]), Integer.valueOf(splitgetHepatitis_A2[1]) - 1, Integer.valueOf(splitgetHepatitis_A2[2]));
            DateHepatitis_A2Minus1Year.add(Calendar.YEAR, -1);

            Calendar DateHepatitis_A2Minus180Day = new GregorianCalendar(Integer.valueOf(splitgetHepatitis_A2[0]), Integer.valueOf(splitgetHepatitis_A2[1]) - 1, Integer.valueOf(splitgetHepatitis_A2[2]));
            DateHepatitis_A2Minus180Day.add(Calendar.DAY_OF_YEAR, -180);

            String[] splitgetHepatitis_B2 = sdf.format(getHepatitis_B2()).split("-");
            Calendar DateHepatitis_B2Minus30Day = new GregorianCalendar(Integer.valueOf(splitgetHepatitis_B2[0]), Integer.valueOf(splitgetHepatitis_B2[1]) - 1, Integer.valueOf(splitgetHepatitis_B2[2]));
            DateHepatitis_B2Minus30Day.add(Calendar.DAY_OF_YEAR, -30);
            Calendar DateHepatitis_B2Minus35Day = new GregorianCalendar(Integer.valueOf(splitgetHepatitis_B2[0]), Integer.valueOf(splitgetHepatitis_B2[1]) - 1, Integer.valueOf(splitgetHepatitis_B2[2]));
            DateHepatitis_B2Minus35Day.add(Calendar.DAY_OF_YEAR, -35);

            String[] splitgetHepatitis_B3 = sdf.format(getHepatitis_B3()).split("-");
            Calendar DateHepatitis_B3Minus5Month = new GregorianCalendar(Integer.valueOf(splitgetHepatitis_B3[0]), Integer.valueOf(splitgetHepatitis_B3[1]) - 1, Integer.valueOf(splitgetHepatitis_B3[2]));
            DateHepatitis_B3Minus5Month.add(Calendar.MONTH, -5);
            Calendar DateHepatitis_B3Minus5Motnth5Day = new GregorianCalendar(Integer.valueOf(splitgetHepatitis_B3[0]), Integer.valueOf(splitgetHepatitis_B3[1]) - 1, Integer.valueOf(splitgetHepatitis_B3[2]));
            DateHepatitis_B3Minus5Motnth5Day.add(Calendar.MONTH, -5);
            DateHepatitis_B3Minus5Motnth5Day.add(Calendar.DAY_OF_YEAR, -5);

            HashSet<Boolean> ValidationSet = new HashSet<>();
//            ValidationSet.add(getShigellvak().after(CurrentDayMinus1Year.getTime()));//Шигелвак не более 1 года с текущей даты 
            if (getShigellvak().before(CurrentDayMinus1Year.getTime())) result.put("Шигелвак - более 1 года", false);
//            ValidationSet.add(getTherapist().after(CurrentDayMinus1Year.getTime()));// Терапевт не более 1 года с текущей даты
            if (getTherapist().before(CurrentDayMinus1Year.getTime()))result.put("Терапевт - более 1 года", false);
//            ValidationSet.add(getOtolaryngologist().after(CurrentDayMinus1Year.getTime()));//ЛОР не более 1 года с текущей даты 
            if (getOtolaryngologist().before(CurrentDayMinus1Year.getTime())) result.put("ЛОР - более 1 года", false);
//            ValidationSet.add(getDentist().after(CurrentDayMinus1Year.getTime()));//Стоматолог не более 1 года с текущей даты 
            if (getDentist().before(CurrentDayMinus1Year.getTime())) result.put("Стоматолог - более 1 года", false);
//            ValidationSet.add(getPsychiatrist().after(CurrentDayMinus1Year.getTime()));//психиатор не более 1 года с текущей даты 
            if (getPsychiatrist().before(CurrentDayMinus1Year.getTime())) result.put("Психиатор - более 1 года", false);
//            ValidationSet.add(getExpertInNarcology().after(CurrentDayMinus1Year.getTime()));//нарколог  не более 1 года с текущей даты 
            if (getExpertInNarcology().before(CurrentDayMinus1Year.getTime())) result.put("Нарколог - более 1 года", false);
//            ValidationSet.add(getDermatovenerologist().after(CurrentDayMinus1Year.getTime()));//дерматовенеролог не более 1 года с текущей даты 
            if (getDermatovenerologist().before(CurrentDayMinus1Year.getTime())) result.put("Дерматовенеролог - более 1 года", false);
//            ValidationSet.add(getFluorography().after(CurrentDayMinus1Year.getTime()));//флюорография не более 1 года с текущей даты 
            if (getFluorography().before(CurrentDayMinus1Year.getTime())) result.put("Флюорография - более 1 года", false);
//            ValidationSet.add(getValidation().after(CurrentDayMinus1Year.getTime()));//Аттестация  не более 1 года с текущей даты   
            if (getValidation().before(CurrentDayMinus1Year.getTime())) result.put("Аттестация - более 1 года", false);
//            ValidationSet.add(isValidationForYear()); // аттестация на год 
            if (!isValidationForYear()) result.put("Аттестация - более 1 года", false);
//            ValidationSet.add(getHelminths().after(CurrentDayMinus1Year.getTime()));//Гельминты  не более 1 года с текущей даты 
            if (getHelminths().before(CurrentDayMinus1Year.getTime())) result.put("Гельминты - более 1 года", false);

//            проработка иммунитета к АДСМ
            // если нет прививки и прошло более 10 лет
            if (!BorneDiseases.contains(EnumBorneDiseases.Diphtheria) && getDiphtheria().before(CurrentDayMinus10Year.getTime())) {
                //ValidationSet.add(!getRubella().equals(new Date(0))); // не равно нулевому значению 1970-01-01   
                // нет прививок
                if (getDiphtheria().equals(new Date(0)))
                    result.put("АДСМ - нет привики", false);
                //прошло более 10 лет с текущей даты
                else if (getDiphtheria().before(CurrentDayMinus10Year.getTime())) result.put("АДСМ - более 10 лет", false);
            }            

//            проработка иммунитета к КРАСНУХЕ
            //Если нет прививки и более 25 лет
            if (!BorneDiseases.contains(EnumBorneDiseases.Rubella) && getEmployeeBerthday().before(CurrentDayMinus25Year.getTime())) {
                //ValidationSet.add(!getRubella().equals(new Date(0))); // не равно нулевому значению 1970-01-01
                if (getRubella().equals(new Date(0))) result.put("Краснуха - нет привики", false);
            }

            //            проработка иммунитета к КОРИ     
            if (!getBorneDiseases().contains(EnumBorneDiseases.Measles) && getEmployeeBerthday().after(CurrentDayMinus55Year.getTime())) {
                if (getMeasles().equals(new Date(0)) && getMeasles_2().equals(new Date(0))) {  // если нет прививок от кори
                    result.put("Корь - нет прививок", false);
                } else if (!getMeasles().equals(new Date(0)) && getMeasles_2().equals(new Date(0))) { // если нет второй прививки от кори
                    if (getMeasles().before(CurrentDayMinus3Month.getTime())) result.put("Корь - более 3х месяцев от текущей даты", false); // не более 3х месяцев от текущей даты
                } else if (!getMeasles().equals(new Date(0)) && !getMeasles_2().equals(new Date(0))) { // если есть обе прививки от кори
                    if (getMeasles().after(DateMeasles_2Minus3Month.getTime())) result.put("Корь - менее 3х месяцев с момента 1 прививки", false); // не менее 3х месяцев с момента 1 прививки
                }
            }
//              проработка иммунитета к Гепатиту А     
            if (!getBorneDiseases().contains(EnumBorneDiseases.Hepatitis_A)) {
                if (getHepatitis_A().equals(new Date(0)) && getHepatitis_A2().equals(new Date(0))) {  // если нет прививок от геппатитаА
//                    ValidationSet.add(false); // getHepatitis_A первая вакцина 
//                    ValidationSet.add(false); // getHepatitis_A2 вторая вакцина 
                    result.put("Гепатит А - нет прививок", false);
                } else if (!getHepatitis_A().equals(new Date(0)) && getHepatitis_A2().equals(new Date(0))) { // если нет второй прививки от геппатитаА
                    //ValidationSet.add(getHepatitis_A().after(CurrentDayMinus1Year.getTime())); // не более 1 год от текущей даты
                    if (getHepatitis_A().before(CurrentDayMinus1Year.getTime())) result.put("Гепатит А - прошло больше 1 года от текущей даты", false);
                } else if (!getHepatitis_A().equals(new Date(0)) && !getHepatitis_A2().equals(new Date(0))) { // если есть обе прививки от геппатитаА
//                    ValidationSet.add(getHepatitis_A().after(DateHepatitis_A2Minus1Year.getTime()) // не более 1 года между 1 и 2 
//                            && getHepatitis_A().before(DateHepatitis_A2Minus180Day.getTime()) // не менее 180 дней между 1 и 2 и
//                            && getHepatitis_A2().after(CurrentDayMinus10Year.getTime()));  // не более 10 лет между 2 и текущей датой  
                    if (getHepatitis_A().before(DateHepatitis_A2Minus1Year.getTime())) result.put("Гепатит А - более 1 года между 1 и 2 прививок", false);
                    if (getHepatitis_A().after(DateHepatitis_A2Minus180Day.getTime())) result.put("Гепатит А - меньше 6 месяцев между 1 и 2 прививок", false);
                    if (getHepatitis_A2().before(CurrentDayMinus10Year.getTime())) result.put("Гепатит А - больше 10 лет от текущей даты", false);
                }
            }
//                проработка иммунитета к ГепатитуВ     
            if (!getBorneDiseases().contains(EnumBorneDiseases.Hepatitis_B) && getEmployeeBerthday().after(CurrentDayMinus55Year.getTime())) { // менее 55 лет, то прививка не нужна
                // если нет прививок от ГепатитуВ 0-1-6
                if (getHepatitis_B().equals(new Date(0)) && getHepatitis_B2().equals(new Date(0)) && getHepatitis_B3().equals(new Date(0))) {
//                        ValidationSet.add(false); // getHepatitis_B первая вакцина 
//                        ValidationSet.add(false); // getHepatitis_B2 вторая вакцина 
//                        ValidationSet.add(false); // getHepatitis_B3 третья вакцина    
                    result.put("Гепатит B - нет прививок", false);
                } else if (!getHepatitis_B().equals(new Date(0)) && getHepatitis_B2().equals(new Date(0)) && getHepatitis_B3().equals(new Date(0))) { // если нет второй прививки от ГепатитуВ
//                        ValidationSet.add(getHepatitis_B().after(CurrentDayMinus35Day.getTime())); // не более 35 дней от текущей даты до 1 прививки
                    if (getHepatitis_B().before(CurrentDayMinus35Day.getTime())) result.put("Гепатит B - более 35 дней от текущей даты", false);
                } else if (!getHepatitis_B().equals(new Date(0)) && !getHepatitis_B2().equals(new Date(0)) && getHepatitis_B3().equals(new Date(0))) {
//                        ValidationSet.add(getHepatitis_B2().after(CurrentDayMinus5Month5Day.getTime()) // не более 5 месяцев и 5 дней от 2 прививки до текущей даты
//                        && getHepatitis_B().before(DateHepatitis_B2Minus30Day.getTime()) // не менее 30 дней между 1 и 2       
//                        && getHepatitis_B().after(DateHepatitis_B2Minus35Day.getTime())); // не более 35 дней между 1 и 2     
                    if (getHepatitis_B2().before(CurrentDayMinus5Month5Day.getTime())) result.put("Гепатит B - более 5 месяцев и 5 дней от 2 прививки до текущей даты", false);
                    if (getHepatitis_B().after(DateHepatitis_B2Minus30Day.getTime())) result.put("Гепатит B - менее 30 дней между 1 и 2 прививкой", false);
                    if (getHepatitis_B().before(DateHepatitis_B2Minus35Day.getTime())) result.put("Гепатит B - более 35 дней между 1 и 2 прививкой", false);
                } else if (!getHepatitis_B().equals(new Date(0)) && !getHepatitis_B2().equals(new Date(0)) && !getHepatitis_B3().equals(new Date(0))) {
//                        ValidationSet.add(getHepatitis_B2().after(DateHepatitis_B3Minus5Motnth5Day.getTime()) // не более 5 месяцев и 5 дней между 3 и 2
//                        && getHepatitis_B2().before(DateHepatitis_B3Minus5Month.getTime()) // не менее 5 месяцев между 3 и 2   
//                        && getHepatitis_B().after(DateHepatitis_B2Minus35Day.getTime()) // не более 35 дней между 1 и 2 
//                        && getHepatitis_B().before(DateHepatitis_B2Minus30Day.getTime())); // не менее 30 дней между 2 и 1     
                    if (getHepatitis_B2().before(DateHepatitis_B3Minus5Motnth5Day.getTime())) result.put("Гепатит B - более 5 месяцев и 5 дней между 3 и 2", false);
                    if (getHepatitis_B2().after(DateHepatitis_B3Minus5Month.getTime())) result.put("Гепатит B - менее 5 месяцев между 3 и 2", false);
                    if (getHepatitis_B().before(DateHepatitis_B2Minus35Day.getTime())) result.put("Гепатит B - более 35 дней между 1 и 2", false);
                    if (getHepatitis_B().after(DateHepatitis_B2Minus30Day.getTime())) result.put("Гепатит B - менее 30 дней между 2 и 1", false);
                }
            }

            // Проверка анализов при трудоустройстве
            if (!getEmploymentDate().equals(new Date(0))) {
                //Кишечная инфекция 
                if (!getIntestinalInfection().equals(new Date(0))) {
                    //ValidationSet.add(getEmploymentDate().after(new Date(getIntestinalInfection().getTime() - 604800000L)));//Кишечная инфекция не более 7 дней с даты трудоустройства       
                    if (getIntestinalInfection().before(new Date(getEmploymentDate().getTime() - 604800000L))) result.put("Кишечная инфекция - более 7 дней с даты трудоустройства", false);
                } 
                //Брюшной тиф 
                if (!getTyphoidFever().equals(new Date(0))) {
//                    ValidationSet.add(getEmploymentDate().after(new Date(getTyphoidFever().getTime() - 604800000L)));//Брюшной тиф   не более 7 дней с даты трудоустройства     
                    if (getTyphoidFever().before(new Date(getEmploymentDate().getTime() - 604800000L))) result.put("Брюшной тиф - более 7 дней с даты трудоустройства", false);
                } 
                //Стаффилокок
                if (!getStaphylococcus().equals(new Date(0))) {
//                    ValidationSet.add(getEmploymentDate().after(new Date(getStaphylococcus().getTime() - 604800000L)));//Стаффилокок  не более  7 дней с даты трудоустройства     
                    if (getStaphylococcus().before(new Date(getEmploymentDate().getTime() - 604800000L))) result.put("Стафилококк - более 7 дней с даты трудоустройства", false);
                } 
            } else {
//                ValidationSet.add(false);
                result.put("Дата трудоустройства не установлена", false);
            }

            Valid = result.containsValue(false);
        } catch (Exception ex) {
            System.out.println("com.Class.MedicalBook.checkValidMedicalBook() - " + ex.getMessage());
            Valid = false;
            result.put("Неизвестная ошибка - "+ ex.getMessage(), false);
        }
        return result;
    }

    public ArrayList<EnumBorneDiseases> StringToEnumBorneDeseases(String String) {
        ArrayList<EnumBorneDiseases> arrayList = new ArrayList<>();
        String[] subString = new String[10];
        if (String != null) {
            if (String.length() > 0) {
                subString = String.split(",");
                for (String subString1 : subString) {
                    arrayList.add(EnumBorneDiseases.valueOf(subString1));
                }
            }
        }
        return arrayList;
    }

    public ArrayList<EnumHepatitisAvaccine> StringToEnumHepatitisAvaccine(String String) {
        ArrayList<EnumHepatitisAvaccine> arrayList = new ArrayList<>();
        String[] subString = new String[10];
        if (String != null) {
            if (String.length() > 0) {
                subString = String.split("-");
                for (String subString1 : subString) {
                    arrayList.add(EnumHepatitisAvaccine.valueOf(subString1));
                }
            }
        }
        return arrayList;
    }

}

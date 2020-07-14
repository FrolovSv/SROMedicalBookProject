/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Class;

import com.Class.MedicalBook.EnumBorneDiseases;
import com.SQL.SQLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author SusPecT
 */
public class MedicalBookTest {
    
    public MedicalBookTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }


    /**
     * Test of checkValidMedicalBook method, of class MedicalBook.
     */
    @org.junit.jupiter.api.Test
    public void testCheckValidMedicalBook() throws ParseException {
        
        
        
        System.out.println("checkValidMedicalBook");
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd"); 

        MedicalBook MedicalBook = new MedicalBook();  
        ArrayList<EnumBorneDiseases> b = MedicalBook.getBorneDiseases();
//        MedicalBook.setBorneDiseases(StringToEnumBorneDeseases("Rubella"));   
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // младше 55 лет
        MedicalBook.setTherapist(new Date());  
        MedicalBook.setOtolaryngologist(new Date());
        MedicalBook.setDentist(new Date());                
        MedicalBook.setExpertInNarcology(new Date());                
        MedicalBook.setPsychiatrist(new Date());                
        MedicalBook.setDermatovenerologist(new Date());                              
        MedicalBook.setFluorography(new Date());                
        MedicalBook.setHelminths(new Date());                
        MedicalBook.setIntestinalInfection(new Date());                
        MedicalBook.setTyphoidFever(new Date());
        MedicalBook.setStaphylococcus(new Date());
        MedicalBook.setValidation(new Date());
        MedicalBook.setShigellvak(new Date());
        MedicalBook.setHepatitis_A(new Date());  // геппатит А
        MedicalBook.setHepatitis_A2(new Date(0)); // геппатит А 
        MedicalBook.setHepatitis_B(new Date()); // геппатит В  
        MedicalBook.setHepatitis_B2(new Date(0));  // геппатит В
        MedicalBook.setHepatitis_B3(new Date(0));  // геппатит В
        MedicalBook.setDiphtheria(new Date());  // ФДСМ
        MedicalBook.setValidationForYear(true);   
        MedicalBook.setRubella(new Date());  // Краснуха  
        MedicalBook.setMeasles(new Date());  // 
        MedicalBook.setMeasles_2(new Date(0)); // 
        MedicalBook.setEmploymentDate(new Date());
        //MedicalBook.setHepatitisAvaccine(StringToEnumHepatitisAvaccine("Wakta"));  
        assertEquals(true, MedicalBook.checkValidMedicalBook());
        
        
        measlesTest(MedicalBook, b);   
        System.out.println("measlesTest PASS");
        rubellaTest(MedicalBook, b); 
        System.out.println("rubellaTest PASS");
        diphtheriaTest(MedicalBook, b);
        System.out.println("diphtheriaTest PASS");
        HepatitisATest(MedicalBook, b);
        System.out.println("HepatitisATest PASS");
        HepatitisBTest(MedicalBook, b);
        System.out.println("HepatitisBTest PASS");
        
        MedicalBook.setEmploymentDate(new Date(new Date().getTime()-518400000L)); // минус 5 дней от фактической даты
        MedicalBook.setIntestinalInfection(new Date());
        assertEquals(true, MedicalBook.checkValidMedicalBook());
        System.out.println("setEmploymentDate() 1 PASS");
        
        MedicalBook.setEmploymentDate(new Date(new Date().getTime()-518400000L)); // минус 5 дней от фактической даты
        MedicalBook.setIntestinalInfection(new Date(new Date().getTime()-604800000L)); // минус 7 дней от фактической даты
        assertEquals(true, MedicalBook.checkValidMedicalBook());
        System.out.println("setEmploymentDate() 2 PASS");
        
//        MedicalBook.setEmploymentDate(new Date(new Date().getTime()-604800000L)); // минус 7 дней от фактической даты
//        MedicalBook.setIntestinalInfection(new Date(new Date().getTime()-518400000L)); // минус 5 дней от фактической даты
//        assertEquals(false, MedicalBook.checkValidMedicalBook());
//        System.out.println("setEmploymentDate() 3 PASS");
        
        MedicalBook.setEmploymentDate(new Date(new Date().getTime()-777600000L)); // минус 9 дней от фактической даты
        MedicalBook.setIntestinalInfection(new Date()); // от фактической даты
        assertEquals(false, MedicalBook.checkValidMedicalBook());
        System.out.println("setEmploymentDate() 4 PASS");
    }

    public ArrayList<MedicalBook.EnumHepatitisAvaccine> StringToEnumHepatitisAvaccine(String String) {
        ArrayList<MedicalBook.EnumHepatitisAvaccine> arrayList = new ArrayList<>();
        String[] subString = new String[10];
        if (String != null) {
            if (String.length() > 0) {
                subString = String.split("-");
                for (String subString1 : subString) {
                    arrayList.add(MedicalBook.EnumHepatitisAvaccine.valueOf(subString1));
                }
            }
        } 
        return arrayList;
    }
    public ArrayList<MedicalBook.EnumBorneDiseases> StringToEnumBorneDeseases(String String) {
        ArrayList<MedicalBook.EnumBorneDiseases> arrayList = new ArrayList<>();
        String[] subString = new String[10];
        if (String != null) {
            if (String.length() > 0) {
                subString = String.split("-");
                for (String subString1 : subString) {
                    arrayList.add(MedicalBook.EnumBorneDiseases.valueOf(subString1));
                }
            }
        } 
        return arrayList;
    }
    
    public void measlesTest(MedicalBook MedicalBook, ArrayList<EnumBorneDiseases> b) throws ParseException{
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        // =========================== проверяем по ЖКВ ===========================  
        MedicalBook.setMeasles(new Date(0));  // ЖКВ по дефолту
        MedicalBook.setMeasles_2(new Date(0)); // ЖКВ по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1965-01-01")); // старше 55 лет
        assertEquals(true, MedicalBook.checkValidMedicalBook()); // при возрасте старше 55 лет прививки не нужны  
        System.out.println("measlesTest() - 1 PASS");
        
        MedicalBook.setMeasles(new Date(0));  // ЖКВ по дефолту
        MedicalBook.setMeasles_2(new Date(0)); // ЖКВ по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // младше 55 лет
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!прививки должны быть при возрасте младше 55 лет
        System.out.println("measlesTest() - 2 PASS");
        
        MedicalBook.setMeasles(new Date(new Date().getTime()-5184000000L));  // ЖКВ текущая дата - 2 месяца 60 дней
        MedicalBook.setMeasles_2(new Date(0)); // ЖКВ по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // младше 55 лет
        assertEquals(true, MedicalBook.checkValidMedicalBook());  // при наличии ТОЛЬКО одной прививки разница от текущей даты не должна быть более 90 дней
        System.out.println("measlesTest() - 3 PASS");
        
        MedicalBook.setMeasles(new Date(new Date().getTime()-26784000000L));  // ЖКВ текущая дата - 10.5 месяца 310 дней
        MedicalBook.setMeasles_2(new Date(0)); // ЖКВ по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // младше 55 лет
        assertEquals(false, MedicalBook.checkValidMedicalBook());  // !!!!при наличии ТОЛЬКО одной прививки разница от текущей даты не должна быть более 90 дней
        System.out.println("measlesTest() - 4 PASS");
        
        MedicalBook.setMeasles(new Date(new Date().getTime()-21600000000L));  // от текущей даты - 250 дней
        MedicalBook.setMeasles_2(new Date(new Date().getTime()-14688000000L)); // от текущей даты - 170 дней (разница 80 дней) 
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // младше 55 лет
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!разница между прививками должна быть не менее 90 дней
        System.out.println("measlesTest() - 5 PASS");
        
        MedicalBook.setMeasles(new Date(new Date().getTime()-26784000000L));  // от текущей даты - 310 дней
        MedicalBook.setMeasles_2(new Date(new Date().getTime()-14688000000L)); // от текущей даты - 170 дней (разница 140 дней)
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // младше 55 лет
        assertEquals(true, MedicalBook.checkValidMedicalBook()); // разница между прививками должна быть не менее 90 дней
        System.out.println("measlesTest() - 6 PASS");
        
        // наличие перенесенного заболевания
        b.add(EnumBorneDiseases.Measles);
        MedicalBook.setBorneDiseases(b);
        MedicalBook.setMeasles(new Date(0));  // ЖКВ по дефолту
        MedicalBook.setMeasles_2(new Date(0)); // ЖКВ по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // младше 55 лет
        assertEquals(true, MedicalBook.checkValidMedicalBook()); // при наличии перенесенного заболевания наличие прививок не обязательно
        System.out.println("measlesTest() - 7 PASS");
        // =========================== =========================== ===========================
        // =========================== =========================== ===========================          
    }    
    public void rubellaTest(MedicalBook MedicalBook, ArrayList<EnumBorneDiseases> b) throws ParseException{
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        // =========================== проверяем по КРАСНУХУ ===========================                
        MedicalBook.setRubella(new Date(0));  // по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1999-01-01")); // младше 25 лет
        assertEquals(true, MedicalBook.checkValidMedicalBook()); // при возрасте младше 25 лет прививки не нужны    
        System.out.println("rubellaTest() - 1 PASS");
        
        MedicalBook.setRubella(new Date(0));  // ЖКВ по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // старше 25 лет
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!прививки должны быть при возрасте старше 25 лет
        System.out.println("rubellaTest() - 2 PASS");
        
        MedicalBook.setRubella(new Date(new Date().getTime()-5184000000L));  // ЖКВ текущая дата - 2 месяца 60 дней
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // старше 25 лет
        assertEquals(true, MedicalBook.checkValidMedicalBook());  // при наличии прививки разница от текущей даты не важна
        System.out.println("rubellaTest() - 3 PASS");
        
        MedicalBook.setRubella(new Date(new Date().getTime()-518400000000L));  // ЖКВ текущая дата - 600 дней
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // старше 25 лет
        assertEquals(true, MedicalBook.checkValidMedicalBook());  // при наличии прививки разница от текущей даты не важна
        System.out.println("rubellaTest() - 4 PASS");
        
        MedicalBook.setRubella(new Date(0));  // 
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // старше 25 лет
        b.add(EnumBorneDiseases.Rubella);// наличие перенесенного заболевания
        MedicalBook.setBorneDiseases(b);        
        assertEquals(true, MedicalBook.checkValidMedicalBook());  // при наличии иммунитета прививка не нужна
        System.out.println("rubellaTest() - 5 PASS");
        // =========================== =========================== ===========================
        // =========================== =========================== ===========================          
    }
    public void diphtheriaTest(MedicalBook MedicalBook, ArrayList<EnumBorneDiseases> b) throws ParseException{
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        // =========================== проверяем по АДСМ ===========================                
        MedicalBook.setDiphtheria(new Date(0));  // по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1999-01-01")); // младше 25 лет
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!вне зависимости от возраста привику нужно деать 1 раз в 10 лет
        System.out.println("diphtheriaTest() - 1 PASS");
        
        MedicalBook.setDiphtheria(new Date(0));  // ЖКВ по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // старше 25 лет
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!вне зависимости от возраста привику нужно деать 1 раз в 10 лет
        System.out.println("diphtheriaTest() - 2 PASS");
        
        MedicalBook.setDiphtheria(new Date(new Date().getTime()-5184000000L));  // АДСМ текущая дата - 2 месяца 60 дней
        assertEquals(true, MedicalBook.checkValidMedicalBook());  // при наличии прививки разница от текущей даты не важна
        System.out.println("diphtheriaTest() - 3 PASS");
        
        MedicalBook.setDiphtheria(new Date(new Date().getTime()-346896000000L));  // АДСМ текущая дата - 11 лет
        assertEquals(false, MedicalBook.checkValidMedicalBook());  // !!!привику нужно деать 1 раз в 10 лет
        System.out.println("diphtheriaTest() - 4 PASS");
        
        MedicalBook.setDiphtheria(new Date(0));  // 
        b.add(EnumBorneDiseases.Diphtheria);// наличие перенесенного заболевания
        MedicalBook.setBorneDiseases(b);        
        assertEquals(true, MedicalBook.checkValidMedicalBook());  // при наличии иммунитета прививка не нужна
        System.out.println("diphtheriaTest() - 5 PASS");
        // =========================== =========================== ===========================
        // =========================== =========================== ===========================          
    }
    public void HepatitisATest(MedicalBook MedicalBook, ArrayList<EnumBorneDiseases> b) throws ParseException{
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        // =========================== проверяем по ГеппатитаА ===========================  
        MedicalBook.setHepatitis_A(new Date(0));  // ГеппатитаА по дефолту
        MedicalBook.setHepatitis_A2(new Date(0)); // ГеппатитаА по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1965-01-01")); // старше 55 лет
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!не зависимо от возраста прививки нунжы 
        System.out.println("HepatitisTest() - 1 PASS");
        
        MedicalBook.setHepatitis_A(new Date(0));  // ГеппатитаA по дефолту
        MedicalBook.setHepatitis_A2(new Date(0)); // ГеппатитаA по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // младше 55 лет
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!не зависимо от возраста прививки нунжы 
        System.out.println("HepatitisTest() - 2 PASS");
        
        MedicalBook.setHepatitis_A(new Date(new Date().getTime()-7776000000L));  // ГеппатитаА текущая дата - 90 дней
        MedicalBook.setHepatitis_A2(new Date(0)); // ГеппатитаA по дефолту
        assertEquals(true, MedicalBook.checkValidMedicalBook());  // при наличии ТОЛЬКО одной прививки разница от текущей даты не должна быть более 1 года
        System.out.println("HepatitisTest() - 3 PASS");
        
        MedicalBook.setHepatitis_A(new Date(new Date().getTime()-32832000000L));  // ГеппатитаА текущая дата - 380 дней
        MedicalBook.setHepatitis_A2(new Date(0)); // ГеппатитаA по дефолту
        assertEquals(false, MedicalBook.checkValidMedicalBook());  // !!!!при наличии ТОЛЬКО одной прививки разница от текущей даты не должна быть более 1 года
        System.out.println("HepatitisTest() - 4 PASS");
        
        MedicalBook.setHepatitis_A(new Date(new Date().getTime()-28512000000L));  // ГеппатитаА текущая дата - 330 дней
        MedicalBook.setHepatitis_A2(new Date(0)); // ГеппатитаA по дефолту
        assertEquals(true, MedicalBook.checkValidMedicalBook());  // при наличии ТОЛЬКО одной прививки разница от текущей даты не должна быть более 1 года
        System.out.println("HepatitisTest() - 5 PASS");
        
        MedicalBook.setHepatitis_A(new Date(new Date().getTime()-44064000000L));  // от текущей даты - 510 дней
        MedicalBook.setHepatitis_A2(new Date(new Date().getTime()-8640000000L)); // от текущей даты - 100 дней (разница 410 дней) 
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!разница между прививками должна быть не более 1 года и не менее 182 дней
        System.out.println("HepatitisTest() - 6 PASS");
        
        MedicalBook.setHepatitis_A(new Date(new Date().getTime()-26784000000L));  // от текущей даты - 310 дней
        MedicalBook.setHepatitis_A2(new Date(new Date().getTime()-14688000000L)); // от текущей даты - 170 дней (разница 140 дней)
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!разница между прививками должна быть не более 1 года и не менее 182 дней
        System.out.println("HepatitisTest() - 7 PASS");
        
        MedicalBook.setHepatitis_A(new Date(new Date().getTime()-38880000000L));  // от текущей даты - 450 дней
        MedicalBook.setHepatitis_A2(new Date(new Date().getTime()-12960000000L)); // от текущей даты - 150 дней (разница 300 дней)
        assertEquals(true, MedicalBook.checkValidMedicalBook()); // разница между прививками должна быть не более 1 года и не менее 182 дней
        System.out.println("HepatitisTest() - 8 PASS");
        
        MedicalBook.setHepatitis_A(new Date(new Date().getTime()-364176000000L));  // от текущей даты - 11 лет и 200 дней
        MedicalBook.setHepatitis_A2(new Date(new Date().getTime()-346896000000L)); // от текущей даты - 11 лет (разница 200 дней)
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // разница между прививками должна быть не более 1 года и не менее 182 дней
        System.out.println("HepatitisTest() - 9 PASS");
        
        MedicalBook.setHepatitis_A(new Date(new Date().getTime()-301104000000L));  // от текущей даты - 9 лет и 200 дней
        MedicalBook.setHepatitis_A2(new Date(new Date().getTime()-283824000000L)); // от текущей даты - 9 лет (разница 200 дней)
        assertEquals(true, MedicalBook.checkValidMedicalBook()); // разница между прививками должна быть не более 1 года и не менее 182 дней
        System.out.println("HepatitisTest() - 10 PASS");
        
        // наличие перенесенного заболевания
        b.add(EnumBorneDiseases.Hepatitis_A);
        MedicalBook.setBorneDiseases(b);
        MedicalBook.setHepatitis_A(new Date(0));  // ГеппатитаА по дефолту
        MedicalBook.setHepatitis_A2(new Date(0)); // ГеппатитаА по дефолту
        assertEquals(true, MedicalBook.checkValidMedicalBook()); // при наличии перенесенного заболевания наличие прививок не обязательно
        System.out.println("measlesTest() - 11 PASS");
        // =========================== =========================== ===========================
        // =========================== =========================== ===========================          
    }    
    public void HepatitisBTest(MedicalBook MedicalBook, ArrayList<EnumBorneDiseases> b) throws ParseException{
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        // =========================== проверяем по ГеппатитаА 0-1-6===========================  
        MedicalBook.setHepatitis_B(new Date(0));  // ГеппатитаB по дефолту
        MedicalBook.setHepatitis_B2(new Date(0)); // ГеппатитаB по дефолту
        MedicalBook.setHepatitis_B3(new Date(0)); // ГеппатитаB по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1965-01-01")); // старше 55 лет
        assertEquals(true, MedicalBook.checkValidMedicalBook()); // старше 55 лет прививки не нужны
        System.out.println("HepatitisBTest() - 1 PASS");
        
        MedicalBook.setHepatitis_B(new Date(0));  // ГеппатитаB по дефолту
        MedicalBook.setHepatitis_B2(new Date(0)); // ГеппатитаB по дефолту
        MedicalBook.setHepatitis_B3(new Date(0)); // ГеппатитаB по дефолту
        MedicalBook.setEmployeeBerthday(ft.parse("1990-01-01")); // младше 55 лет
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!младше 55 лет прививки не нужны 
        System.out.println("HepatitisBTest() - 2 PASS");
        
        MedicalBook.setHepatitis_B(new Date(new Date().getTime()-2678400000L));  // ГеппатитаА текущая дата минус 31 дней 
        MedicalBook.setHepatitis_B2(new Date(0)); // ГеппатитаB по дефолту
        MedicalBook.setHepatitis_B3(new Date(0)); // ГеппатитаB по дефолту
        assertEquals(true, MedicalBook.checkValidMedicalBook());  // при наличии ТОЛЬКО ПЕРВОЙ прививки разница от текущей даты (30 - 35 дней
        System.out.println("HepatitisBTest() - 3 PASS");
        
        MedicalBook.setHepatitis_B(new Date(new Date().getTime()-3196800000L));  // ГеппатитаА текущая дата минус 37 дней
        MedicalBook.setHepatitis_B2(new Date(0)); // ГеппатитаB по дефолту
        MedicalBook.setHepatitis_B3(new Date(0)); // ГеппатитаB по дефолту
        assertEquals(false, MedicalBook.checkValidMedicalBook());  // !!!!при наличии ТОЛЬКО одной прививки разница от текущей даты (30 - 35 дней)
        System.out.println("HepatitisBTest() - 4 PASS");        
        
        MedicalBook.setHepatitis_B(new Date(new Date().getTime()-15552000000L-86400000l));  // от текущей даты минус 6 месяца И 1 день
        MedicalBook.setHepatitis_B2(new Date(new Date().getTime()-12960000000L+86400000l)); // от текущей даты минус 4,9 месяцев дней (разница 1 месяцев и 1 день) 
        MedicalBook.setHepatitis_B3(new Date(0)); // ГеппатитаB по дефолту
        assertEquals(true, MedicalBook.checkValidMedicalBook()); // разница между прививками должна быть не более 1 месяца и между 2 (5 мес - 5 мес 5 дней)
        System.out.println("HepatitisBTest() - 5 PASS");
        
        MedicalBook.setHepatitis_B(new Date(new Date().getTime()-15552000000L));  // от текущей даты минус 6 месяц
        MedicalBook.setHepatitis_B2(new Date(new Date().getTime()-12960000000L)); // от текущей даты минус 5 месяцев дней (разница 1 месяц 1 день) 
        MedicalBook.setHepatitis_B3(new Date(0)); // ГеппатитаB по дефолту
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // разница между прививками должна быть не более 1 месяца и между 2 и тек не более 5 мес
        System.out.println("HepatitisBTest() - 6 PASS");
        
        MedicalBook.setHepatitis_B(new Date(new Date().getTime()-20736000000L));  // от текущей даты минус 8 месяцев
        MedicalBook.setHepatitis_B2(new Date(new Date().getTime()-18144000000L)); // от текущей даты минус 7 месяцев (разница 1 месяц) 
        MedicalBook.setHepatitis_B3(new Date(0)); // ГеппатитаB по дефолту
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!// разница между прививками должна быть не более 1 месяца и между 2 и тек не более 5 мес
        System.out.println("HepatitisBTest() - 7 PASS");
        
        MedicalBook.setHepatitis_B(new Date(new Date().getTime()-777600000L));  // от текущей даты минус 9 месяцев
        MedicalBook.setHepatitis_B2(new Date(new Date().getTime()-604800000L)); // от текущей даты минус 7 месяцев (разница 2 месяца) 
        MedicalBook.setHepatitis_B3(new Date(0)); // ГеппатитаB по дефолту
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!// разница между прививками должна быть не более 1 месяца и между 2 и тек не более 5 мес
        System.out.println("HepatitisBTest() - 8 PASS");
        
        MedicalBook.setHepatitis_B(new Date(new Date().getTime()-18144000000L-86400000l-86400000l-86400000l-86400000l));  // от текущей даты минус 7 месяцев 
        MedicalBook.setHepatitis_B2(new Date(new Date().getTime()-15552000000L-86400000l-86400000l-86400000l)); // от текущей даты минус 6 месяцев  
        MedicalBook.setHepatitis_B3(new Date(new Date().getTime()-2592000000L)); // от текущей даты минус 1 месяцев  
        assertEquals(true, MedicalBook.checkValidMedicalBook()); // разница между прививками должна быть не более 1 месяца и между 2 и тек не более 5 мес
        System.out.println("HepatitisBTest() - 9 PASS");
        
        MedicalBook.setHepatitis_B(new Date(new Date().getTime()-18144000000L));  // от текущей даты минус 7 месяцев 
        MedicalBook.setHepatitis_B2(new Date(new Date().getTime()-15552000000L)); // от текущей даты минус 6 месяцев  
        MedicalBook.setHepatitis_B3(new Date(new Date().getTime()-2592000000L)); // от текущей даты минус 1 месяцев  
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // разница между прививками должна быть не более 1 месяца и между 2 и тек не более 5 мес
        System.out.println("HepatitisBTest() - 10 PASS");
        
        MedicalBook.setHepatitis_B(new Date(new Date().getTime()-20736000000L));  // от текущей даты минус 8 месяцев 
        MedicalBook.setHepatitis_B2(new Date(new Date().getTime()-18144000000L)); // от текущей даты минус 7 месяцев  
        MedicalBook.setHepatitis_B3(new Date(new Date().getTime()-2592000000L)); // от текущей даты минус 1 месяцев  
        assertEquals(false, MedicalBook.checkValidMedicalBook()); // !!!// разница между прививками должна быть не более 1 месяца и между 2 и тек не более 5 мес
        System.out.println("HepatitisBTest() - 11 PASS");
        
        // наличие перенесенного заболевания
        b.add(EnumBorneDiseases.Hepatitis_B);
        MedicalBook.setBorneDiseases(b);
        MedicalBook.setHepatitis_B(new Date(0));  // ГеппатитаА по дефолту
        MedicalBook.setHepatitis_B2(new Date(0)); // ГеппатитаА по дефолту
        MedicalBook.setHepatitis_B3(new Date(0)); // ГеппатитаА по дефолту
        assertEquals(true, MedicalBook.checkValidMedicalBook()); // при наличии перенесенного заболевания наличие прививок не обязательно
        System.out.println("HepatitisBTest() - 13 PASS");
        // =========================== =========================== ===========================
        // =========================== =========================== ===========================          
    }    
    
    
}

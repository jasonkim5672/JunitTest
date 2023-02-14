package com.healthycoderapp;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BMICalculatorTest{

    private String environment = "prod";

    @BeforeAll
    static void beforeAll(){
        System.out.println("Before all unit tests.");

    }

    @AfterAll
    static void afterAll(){
        System.out.println("After all unit tests.");

    }

    @Nested
    class IsDietRecommendedTests{

        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvFileSource(resources = "/diet-recommended-input-data.csv" , numLinesToSkip=1 )
        void should_ReturnTrue_when_DietRecommended_withCsvFileSource(Double coderWeight , Double coderHeight ){
            //given
            double weight = coderWeight;
            double height = coderHeight;
            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);

        }



        @ParameterizedTest(name = "weight={0}, height={1}")
        @CsvSource(value = {"89.0,1.72 ", "95.0,1.75" , "110.0,1.78"}  )
        void should_ReturnTrue_when_DietRecommended_withCsvSource(Double coderWeight , Double coderHeight ){
            //given
            double weight = coderWeight;
            double height = coderHeight;
            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);

        }

        @ParameterizedTest
        @ValueSource(doubles = { 89.0,95.0,110.0})
        void should_ReturnTrue_when_DietRecommended_withValueSource(Double coderWeight){
            //given
            double weight = coderWeight;
            double height = 1.72;
            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);

        }

        @Test
        void should_ReturnTrue_when_DietRecommended(){
            //given
            double weight = 85.0;
            double height = 1.72;
            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);

        }


        @Test
        void should_ReturnFalse_when_DietNotRecommended(){
            //given
            double weight = 50.0;
            double height = 1.92;
            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertFalse(recommended);

        }

        @Test
        void should_ThrowArithmeticException_when_HeightZero(){
            //given
            double weight = 50.0;
            double height =0.00;
            //when
            Executable executable =  ()-> BMICalculator.isDietRecommended(weight, height);

            //then

            assertThrows(ArithmeticException.class,executable);

        }





    }

    @Nested
    @DisplayName("{{}}Sample Inner Class")
    class FindCoderWithWorstBMITest{


        @Test
        @DisplayName(">>> sample ")
        // @Disabled
        @DisabledOnOs(OS.MAC)
        void should_ReturnCoderWithWorstBMI_when_CodeListNotEmpty(){
            //given

            List<Coder> coders = new ArrayList<Coder>();
            coders.add(new Coder(1.80,60.0));
            coders.add(new Coder(1.82,98.0));
            coders.add(new Coder(1.82,64.7));

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertAll(
                    () -> assertEquals(1.82, coderWorstBMI.getHeight()),
                    () -> assertEquals(98.0, coderWorstBMI.getWeight())
            );
        }

        @Test
        void should_ReturnCoderWithWorstBMIIn1Ms_When_CodeListHas1000Elements(){

            //given
            List<Coder> coders = new ArrayList<Coder>();
            for(int i = 0; i < 10000; i ++) {
                coders.add(new Coder(1.0+i , 10.0+i));
            }
            //when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);
            //then
            assertTimeout(Duration.ofMillis(5) , executable);

        }


        @Test
        void should_ReturnCoderWithWorstBMIIn1Ms_When_CodeListHas1000Elements_(){

            //given
            assumeTrue(BMICalculatorTest.this.environment.equals("prod"));
            List<Coder> coders = new ArrayList<Coder>();
            for(int i = 0; i < 10000; i ++) {
                coders.add(new Coder(1.0+i , 10.0+i));
            }
            //when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);
            //then
            assertTimeout(Duration.ofMillis(5) , executable);

        }




        @Test
        void should_ReturnNullWorstBMI_when_CodeListNotEmpty(){
            //given

            List<Coder> coders = new ArrayList<Coder>();
            // coders.add(new Coder(1.80,60.0));
            // coders.add(new Coder(1.82,98.0));
            // coders.add(new Coder(1.82,64.7));

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertNull(coderWorstBMI);

        }


    }

    @Nested
    class GetBMIScoresTest{
        @Test
        void should_ReturnCorrectBMIScoreArray_When_CoderListNotEmpty(){
            // given
            List<Coder> coders = new ArrayList<Coder>();
            coders.add(new Coder(1.80,60.0));
            coders.add(new Coder(1.82,98.0));
            coders.add(new Coder(1.82,64.7));
            double[] expected = {18.52,29.59, 19.53};

            //when
            double[] bmiScores = BMICalculator.getBMIScores(coders);

            //then
            assertArrayEquals(expected, bmiScores);

        }

    }





}
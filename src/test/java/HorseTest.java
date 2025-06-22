import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HorseTest {


    @ParameterizedTest
    @NullSource
    void testConstructorIfNameIsNull(String nullName) {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Horse horse = new Horse(nullName, 1.0, 10.0);
                }
        );
        assertEquals("Name cannot be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "", "\t", "\n"})
    void testConstructorIfNameIsBlank(String blankName) {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Horse horse = new Horse(blankName, 1.0, 10.0);
                }
        );
        assertEquals("Name cannot be blank.", exception.getMessage());
    }

    @Test
    void testSpeedParameterIfNegative(){
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Horse horse = new Horse("Horse", -1.0, 10.0);
                }
        );
        assertEquals("Speed cannot be negative.", exception.getMessage());
    }

    @Test
    void testDistanceParameterIfNegative(){
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Horse horse = new Horse("Horse", 1.0, -10.0);
                }
        );
        assertEquals("Distance cannot be negative.", exception.getMessage());
    }


    @Test
    void getName() {
        Horse horse = new Horse("Paddy", 1.0, 10.0);
        assertEquals("Paddy", horse.getName());
    }

    @Test
    void getSpeed() {
        Horse horse = new Horse("Paddy", 111.234, 10.0);
        assertEquals(111.234, horse.getSpeed());
    }

    @Test
    void getDistance() {
        Horse horse = new Horse("Paddy", 111.234, 8765.3475);
        assertEquals(8765.3475, horse.getDistance());
        Horse horseAnother = new Horse("Paddy", 111.234);
        assertEquals(0, horseAnother.getDistance());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.3, 0.4, 0.8})
    void move(double randomDouble) {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)){
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(randomDouble);
            Horse horse = new Horse("Paddy", 3.3, 10.0);
            horse.move();
            assertEquals(3.3*randomDouble+10.0, horse.getDistance());
            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9));
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0.2, 0.9",
            "0.5, 0.7",
            "0.1, 0.8"
    })
    void getRandomDouble(double min, double max) {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)){
            double randomDouble = ThreadLocalRandom.current().nextDouble(min, max);
            mockedStatic.when(() -> Horse.getRandomDouble(min, max)).thenReturn(randomDouble);
            assertEquals(randomDouble, Horse.getRandomDouble(min, max));
        }
    }
}
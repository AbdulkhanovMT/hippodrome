import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HippodromeTest {

    @Mock
    Hippodrome hippodrome;

    @ParameterizedTest
    @NullSource
    void testConstructorForNull(List<Horse> horses) {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    hippodrome = new Hippodrome(horses);
                }
        );
        assertEquals("Horses cannot be null.", exception.getMessage());
    }

    @Test
    void testConstructorIfListIsEmpty() {
        List<Horse> horses = Collections.emptyList();
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    Hippodrome hippodrome = new Hippodrome(horses);
                }
        );
        assertEquals("Horses cannot be empty.", exception.getMessage());
    }

    @Test
    void getHorses() {
        String nameTemplate = "Henry ";
        double doubleValue = 5.0;
        List<Horse> initHorses = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            initHorses.add(new Horse(nameTemplate + i + 1, doubleValue + 1.0 * i / 2, doubleValue));
        }
        Hippodrome hippodrome = new Hippodrome(initHorses);
        assertEquals(initHorses, hippodrome.getHorses());
    }

    @Test
    void move() {
        List<Horse> horses = new ArrayList<>();
        Horse mockedHorse = mock(Horse.class);
        doNothing().when(mockedHorse).move();
        for (int i = 0; i < 50; i++) {
            horses.add(mockedHorse);
        }
        Hippodrome hippodrome = new Hippodrome(horses);
        hippodrome.move();
        verify(mockedHorse, times(50)).move();
    }

    @Test
    void getWinner() {
        Horse billyHorse = new Horse("Billy", 2.0, 30);
        Horse willyHorse = new Horse("Willy", 2.0, 20);
        Horse dillyHorse = new Horse("Dilly", 2.0, 50);
        List<Horse> horses = List.of(billyHorse, willyHorse, dillyHorse);
        when(hippodrome.getWinner()).thenReturn(horses.stream().max(Comparator.comparing(Horse::getDistance)).get());
        assertEquals(dillyHorse, hippodrome.getWinner());
    }
}
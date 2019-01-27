package uk.co.serin.thule.utils.utils;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class ResourceUtilsTest {

    @Test
    public void private_constructor_executes_without_exception() {
        assertThat(BeanUtils.instantiateClass(ResourceUtils.class)).isNotNull();
    }

    @Test
    public void when_reading_from_classpath_then_it_will_return_file_content() {

        //When
        var content = ResourceUtils.readFromClasspath("testLoadedFile.txt");

        //Then
        assertThat(content).isEqualTo("aa\nbb\ncc");
    }

    @Test
    public void when_reading_nonexisting_resource_then_runtime_exception_is_rethrown() {

        //When
        var thrown = catchThrowable(() -> ResourceUtils.readFromClasspath("nonexistentFile.txt"));

        //Then
        assertThat(thrown).isExactlyInstanceOf(IllegalStateException.class);
    }
}
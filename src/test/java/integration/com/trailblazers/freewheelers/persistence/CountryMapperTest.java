package integration.com.trailblazers.freewheelers.persistence;

import com.trailblazers.freewheelers.mappers.CountryMapper;
import com.trailblazers.freewheelers.model.Country;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CountryMapperTest extends MapperTestBase{

    private CountryMapper countryMapper;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        countryMapper = getSqlSession().getMapper(CountryMapper.class);
    }

    @Test
    public void shouldInsertAndGetCountries() throws Exception {
        Country country = someCountry();

        countryMapper.insert(country);
        Country fetchedFromDB = countryMapper.getByName(country.getName());

        assertThat(fetchedFromDB.getName(),is(someCountry().getName()));
    }

    @Test
    public void shouldFindAllCountries() throws Exception {

        int before = countryMapper.findAll().size();

        countryMapper.insert(someCountry());
        countryMapper.insert(someCountry());
        countryMapper.insert(someCountry());

        assertThat(countryMapper.findAll().size(), is(before + 3));
    }

    private Country someCountry() {
        return new Country(0,"Test Country");
    }
}
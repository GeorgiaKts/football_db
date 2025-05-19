package cs.uoi.football.statistics.ServiceTest.Create_And_Load_Test;

import cs.uoi.football.statistics.Service.Create_And_Load.Create_Tables_Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.mockito.*;

import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.*;

public class Create_Tables_Service_Test {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private Create_Tables_Service createTablesService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTablesExecutesAllStatements() {
        createTablesService.createTables();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate, times(6)).execute(captor.capture());

        var executedSqlStatements = captor.getAllValues();

        assertTrue(executedSqlStatements.stream().anyMatch(sql -> sql.contains("CREATE TABLE IF NOT EXISTS countries")));
        assertTrue(executedSqlStatements.stream().anyMatch(sql -> sql.contains("CREATE TABLE IF NOT EXISTS teams")));
        assertTrue(executedSqlStatements.stream().anyMatch(sql -> sql.contains("CREATE TABLE IF NOT EXISTS former_names")));
        assertTrue(executedSqlStatements.stream().anyMatch(sql -> sql.contains("CREATE TABLE IF NOT EXISTS results")));
        assertTrue(executedSqlStatements.stream().anyMatch(sql -> sql.contains("CREATE TABLE IF NOT EXISTS shootouts")));
        assertTrue(executedSqlStatements.stream().anyMatch(sql -> sql.contains("CREATE TABLE IF NOT EXISTS goalscorers")));
    }

}

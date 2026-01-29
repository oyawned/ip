package sheng.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import sheng.exception.ShengException;

public class ParserTest {

    @Test
    public void getCommand_validByeCommand_returnsCommandBye() throws ShengException {
        assertEquals(Command.BYE, Parser.getCommand("bye"));
    }

    @Test
    public void getCommand_validListCommand_returnsCommandList() throws ShengException {
        assertEquals(Command.LIST, Parser.getCommand("list"));
    }

    @Test
    public void getCommand_validTodoCommand_returnsCommandTodo() throws ShengException {
        assertEquals(Command.TODO, Parser.getCommand("todo read book"));
    }

    @Test
    public void getCommand_validDeadlineCommand_returnsCommandDeadline() throws ShengException {
        assertEquals(Command.DEADLINE, Parser.getCommand("deadline return book /by 2024-01-01 1800"));
    }

    @Test
    public void getCommand_validEventCommand_returnsCommandEvent() throws ShengException {
        assertEquals(Command.EVENT, Parser.getCommand("event meeting /from 2024-01-01 1400 /to 2024-01-01 1600"));
    }

    @Test
    public void getCommand_emptyInput_throwsShengException() {
        assertThrows(ShengException.class, () -> Parser.getCommand(""));
        assertThrows(ShengException.class, () -> Parser.getCommand("   "));
    }

    @Test
    public void getCommand_invalidCommand_throwsShengException() {
        assertThrows(ShengException.class, () -> Parser.getCommand("invalid"));
    }

    @Test
    public void getCommand_todoWithoutDescription_throwsShengException() {
        assertThrows(ShengException.class, () -> Parser.getCommand("todo"));
    }

    @Test
    public void getCommand_deadlineWithoutBy_throwsShengException() {
        assertThrows(ShengException.class, () -> Parser.getCommand("deadline return book"));
    }

    @Test
    public void getCommand_eventWithoutFrom_throwsShengException() {
        assertThrows(ShengException.class, () -> Parser.getCommand("event meeting /to 2024-01-01 1600"));
    }

    @Test
    public void getCommand_eventWithoutTo_throwsShengException() {
        assertThrows(ShengException.class, () -> Parser.getCommand("event meeting /from 2024-01-01 1400"));
    }

    @Test
    public void getTaskIndex_validMarkCommand_returnsCorrectIndex() throws ShengException {
        assertEquals(0, Parser.getTaskIndex("mark 1", 5));
        assertEquals(2, Parser.getTaskIndex("mark 3", 5));
    }

    @Test
    public void getTaskIndex_validDeleteCommand_returnsCorrectIndex() throws ShengException {
        assertEquals(0, Parser.getTaskIndex("delete 1", 3));
        assertEquals(1, Parser.getTaskIndex("delete 2", 3));
    }

    @Test
    public void getTaskIndex_invalidIndex_throwsShengException() {
        assertThrows(ShengException.class, () -> Parser.getTaskIndex("mark 0", 5));
        assertThrows(ShengException.class, () -> Parser.getTaskIndex("mark 10", 5));
        assertThrows(ShengException.class, () -> Parser.getTaskIndex("delete -1", 3));
    }

    @Test
    public void getTaskIndex_nonNumericIndex_throwsShengException() {
        assertThrows(ShengException.class, () -> Parser.getTaskIndex("mark abc", 5));
        assertThrows(ShengException.class, () -> Parser.getTaskIndex("delete xyz", 3));
    }

    @Test
    public void getTodoDescription_validInput_returnsDescription() throws ShengException {
        assertEquals("read book", Parser.getTodoDescription("todo read book"));
        assertEquals("buy groceries", Parser.getTodoDescription("todo buy groceries"));
    }

    @Test
    public void getTodoDescription_emptyDescription_throwsShengException() {
        assertThrows(ShengException.class, () -> Parser.getTodoDescription("todo "));
    }

    @Test
    public void getDeadlineDescription_validInput_returnsDescription() throws ShengException {
        assertEquals("return book", Parser.getDeadlineDescription("deadline return book /by 2024-01-01 1800"));
    }

    @Test
    public void getDeadlineBy_validInput_returnsDate() throws ShengException {
        assertEquals("2024-01-01 1800", Parser.getDeadlineBy("deadline return book /by 2024-01-01 1800"));
    }

    @Test
    public void getEventDescription_validInput_returnsDescription() throws ShengException {
        assertEquals("project meeting", Parser.getEventDescription("event project meeting /from 2024-01-01 1400 /to 2024-01-01 1600"));
    }

    @Test
    public void getEventFrom_validInput_returnsFromDate() throws ShengException {
        assertEquals("2024-01-01 1400", Parser.getEventFrom("event project meeting /from 2024-01-01 1400 /to 2024-01-01 1600"));
    }

    @Test
    public void getEventTo_validInput_returnsToDate() throws ShengException {
        assertEquals("2024-01-01 1600", Parser.getEventTo("event project meeting /from 2024-01-01 1400 /to 2024-01-01 1600"));
    }
}

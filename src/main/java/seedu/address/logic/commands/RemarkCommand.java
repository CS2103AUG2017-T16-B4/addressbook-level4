package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.Remark;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

import java.util.List;

import static seedu.address.logic.commands.EditCommand.MESSAGE_DUPLICATE_PERSON;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

public class RemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "remark";
    public Remark remark;
    public Index index;

    public RemarkCommand(Remark args, Index index){
        remark = args;
        this.index=index;
    }

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a remark to a person in the address book. "
            + "Parameters: "
            + "INDEX "
            + PREFIX_REMARK + "[REMARK]\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 " + PREFIX_REMARK
            + "Likes to drink coffee.";

    public static final String REMARK_EDIT_SUCCESS = "Remark added.";
    public static final String REMARK_CLEAR_SUCCESS = "Remark cleared.";

    public CommandResult executeUndoableCommand() throws CommandException{
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                                personToEdit.getAddress(), remark, personToEdit.getTags());

        try {
            model.updatePerson(personToEdit, editedPerson);
            } catch (DuplicatePersonException dpe) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            } catch (PersonNotFoundException pnfe) {
                throw new AssertionError("The target person cannot be missing");
            }

            return new CommandResult(generateSuccessMessage(editedPerson));
        }

    private String generateSuccessMessage(ReadOnlyPerson personToEdit) {
        if (!remark.remarkText.isEmpty()) {
            return String.format(REMARK_EDIT_SUCCESS, personToEdit);
        } else {
            return String.format(REMARK_CLEAR_SUCCESS, personToEdit);
        }
    }
}

fun main() {

    val note1 = Note(
        ownerId = 1, title = "заметка", text = "вот такая вот заметочка",
        privacy = Privacy.ALL, comment_privacy = Privacy.ALL
    )

    val note2 = Note(
        ownerId = 1, title = "заметка2", text = "вот такая вот заметочка2",
        privacy = Privacy.ALL, comment_privacy = Privacy.ALL
    )

    val comment = Comment(noteId = 1, ownerId = 1, replyTo = null, message = "+")
    val comment2 = Comment(noteId = 1, ownerId = 1, replyTo = null, message = "+++")

    val notes = NotesService()
    notes.add(note1)
    notes.add(note2)
    notes.createComment(comment)
    notes.createComment(comment2)

    val listIdNotes = listOf<Int>(1, 2)

    val y = notes.getComments(noteId = 1, ownerId = 1, count = 2)
    println(y)

    println(notes.get(noteId = listIdNotes, userId = 1, count = 20))
    notes.getById(3, 1)


    notes.printComments()

    notes.deleteComment(2, 1)
    notes.printComments()

    notes.editComment(2, 1, "отредактировано")
    notes.printComments()

    notes.restoreComment(1, 1)


    notes.printNotes()

    notes.delete(2)

    notes.printNotes()
}
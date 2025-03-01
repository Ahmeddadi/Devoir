package modeles;

public class Evaluation {
    private int employeId;
    private String commentaire;
    private int note;

    public Evaluation(int employeId, String commentaire, int note) {
        this.employeId = employeId;
        this.commentaire = commentaire;
        this.note = note;
    }

    public int getEmployeId() { return employeId; }
    public String getCommentaire() { return commentaire; }
    public int getNote() { return note; }
}

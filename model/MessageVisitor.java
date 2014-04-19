package model;

public interface MessageVisitor {
	public void visit(PrivateMessage message);
	public void visit(RoomMessage message);
}

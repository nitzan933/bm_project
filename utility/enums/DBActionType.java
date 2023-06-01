package utility.enums;

public enum DBActionType {
	UNKNOWN_ACTION("UNKNOWN_ACTION"),
	UPDATE_EXISTING_DATA_IN_DB("UPDATE_EXISTING_DATA_IN_DB"),
	WRITE_NEW_DATA_TO_DB("WRITE_NEW_DATA_TO_DB");
	
	private String actionType;
	
	private DBActionType(String request) {
		this.actionType = request;
	}
	
	@Override
	public String toString() {
		return actionType;
	}
}

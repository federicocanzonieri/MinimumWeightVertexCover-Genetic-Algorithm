
public enum TypeSelection {

	MU_LAMBDA("mu_lambda"),
    PR("prop_size"),
    GS("general_selection");

    public final String label;

    private TypeSelection(String label) {
        this.label = label;
    }
    
    public String get_label() {
    	return this.label;
    }
	
	
}

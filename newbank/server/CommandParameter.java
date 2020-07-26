package newbank.server;

public class CommandParameter {
	private Parameter parameter;
	private String value;
	private String prompt;
	private String type;
	
	public CommandParameter(Parameter parameter, String value, String prompt, String type) {
		super();
		this.parameter = parameter;
		this.value = value;
		this.prompt = prompt;
		this.type = type;
	}
	public Parameter getParameter() {
		return parameter;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public String getPrompt() {
		return prompt;
	}
	public String getType() {
		return type;
	}
}
	
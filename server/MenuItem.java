package newbank.server;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MenuItem {
	private MenuOptions menuOption;
	private Command command;
	private String label;
	private String description;
	private Map<Parameter, CommandParameter> commandParameters;
	private Map<String, MenuItem> subMenuItems;

	//This really should not be called directly. 
	//How should this constructor be implemented to be usable locally only?
	public MenuItem() {
		this.commandParameters = new LinkedHashMap<Parameter,CommandParameter>();
		this.subMenuItems = new LinkedHashMap<String, MenuItem>();
	}
	
	public MenuItem(MenuOptions menuOption, Command command,String label, String description) {
		this();
		this.menuOption = menuOption;
		this.command = command;
		this.label = label;
		this.description = description;
	}
	
	public MenuItem(MenuOptions menuOption, Command command,String label, String description, HashMap<Parameter,CommandParameter> commandParameters) {
		this(menuOption, command, label, description);
		this.setCommandParameters(commandParameters);
	}
	
	public MenuOptions getMenuOption() {
		return menuOption;
	}
	
	public String getLabel() {
		return label;
	}
	
	public Command getCommand() {
		return command;
	}
	
	public String getDescription() {
		return description;
	}

	public Map<Parameter, CommandParameter> getCommandParameters() {
		return commandParameters;
	}
	
	public Map<String, MenuItem> getSubMenuItems() {
		return subMenuItems;
	}

	public void setMenuOption(MenuOptions menuOption) {
		this.menuOption = menuOption;
	}

	public void setCommandParameters(HashMap<Parameter, CommandParameter> commandParameters) {
		if (commandParameters != null) 
			this.commandParameters = commandParameters;
	}
	
	public void setSubMenuItems(Map<String, MenuItem> subMenuItems) {
		this.subMenuItems = subMenuItems;
	}
	
	public void addCommandParameter(CommandParameter commandParameter) {
		if (commandParameter!=null && commandParameter.getParameter()!=null) 
			this.commandParameters.put(commandParameter.getParameter(), commandParameter);
	}
	
	public void addSubMenu(MenuItem menuItem) {
		if (subMenuItems!=null) 
			this.getSubMenuItems().put(menuItem.getLabel(), menuItem);
	}
	
//	public void printSubMenu(PrintWriter out) {
//		StringBuilder stringBuilder = new StringBuilder();
//        
//        getSubMenuItems().forEach((menuOption, menuItem) -> {
//        	int count = 0;
//        	stringBuilder.append(++count + "\t" + menuItem.getLabel() +"\n");
//        });
//        
//        stringBuilder.append("9.\tQuit" +"\n");
//        out.println(stringBuilder.toString());
//    }

	public boolean hasSubMenu() {
		if (getSubMenuItems().isEmpty()) return false;
		return true;
	}
}

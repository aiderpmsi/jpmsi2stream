package aider.org.pmsi.parser.linestypes;

import java.util.regex.Pattern;

/**
 * Défini l'architecture pour créer des patrons de lignes pmsi avec :
 * <ul>
 *  <li>Les noms des éléments</li>
 *  <li>Les regex des éléments</li>
 *  <li>Les contenus des éléments</li>
 * </ul>
 * @author delabre
 *
 */
public class PmsiLineTypeImpl implements PmsiLineType {

	private Pattern pattern;
	
	private String[] names;
	
	private String[][] transforms;
	
	private String name;
	
	private String[] content;
	
	public PmsiLineTypeImpl(String name, Pattern pattern, String[] names, String[][] transforms) {
		this.name = name;
		this.pattern = pattern;
		this.names = names;
		this.transforms = transforms;
		this.content = new String[names.length];
	}
	
	@Override
	public Pattern getPattern() {
		return pattern;
	}
	
	@Override
	public String[] getNames() {
		return names;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setContent(int index, String content) {
		this.content[index] = content;
	}
	
	@Override
	public String[] getContent() {
		if (transforms == null)
			return content;
		else {
			String[] modContent = new String[names.length];
			for (int i = 0 ; i < names.length ; i++) {
				if (transforms[i][0] == null)
					modContent[i] = content[i];
				else {
					modContent[i] = content[i].replaceFirst(transforms[i][0], transforms[i][1]);
				}
			}
			return modContent;
		}
	}
}

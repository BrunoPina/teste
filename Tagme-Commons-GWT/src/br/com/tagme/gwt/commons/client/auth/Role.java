package br.com.tagme.gwt.commons.client.auth;

public class Role{

	private String resource = WILDCARD_TOKEN;
	private String instance = WILDCARD_TOKEN;
	private String action = WILDCARD_TOKEN;

	private static final String WILDCARD_TOKEN = "*";
	private static final String PART_DIVIDER_TOKEN = ":";
	private static final String SUBPART_DIVIDER_TOKEN = ",";
	private static final String ANYTHING_TOKEN = "_";
	
	public Role(){}
	
	public Role(String role){
		String[] parts = role.split(PART_DIVIDER_TOKEN);
		if(parts.length == 1){
			setResource(parts[0]);
		}else if(parts.length == 2){
			setResource(parts[0]);
			setInstance(parts[1]);
		}else if(parts.length > 2){
			setResource(parts[0]);
			setInstance(parts[1]);
			setAction(parts[2]);
		}
	}
	
	public Role(String resource, String instance, String action) {
		setResource(resource);
		setInstance(instance);
		setAction(action);
	}
	
	public String getResource(){
		return this.resource;
	}
	
	public void setResource(String resource) {
		if(!"".equals(resource) && resource != null){
			this.resource = resource;
		}
	}

	public String getInstance(){
		return this.instance;
	}
	
	public void setInstance(String instance) {
		if(!"".equals(instance) && instance != null){
			this.instance = instance;
		}
	}

	public String getAction(){
		return this.action;
	}
	
	public void setAction(String action) {
		if(!"".equals(action) && action != null){
			this.action = action;
		}
	}

	@Override
	public String toString() {
		return new StringBuffer(resource).append(PART_DIVIDER_TOKEN).append(instance).append(PART_DIVIDER_TOKEN).append(action).toString();
	}
	
	/**
	 * @param impliedRole a checagem se uma role implica em outra deve ser feita tendo a role implicada (no caso, impliedRole) mais especifica possivel. 
	 * Assim, evite utilizar ',' separando actions na impliedRole, pois, caso o faça, a role mestre devera dar acesso a TODAS as actions separadas por ",". 
	 * 
	 * @return <code>true</code> se esta role implicar na impliedRole
	 * <code>false</code> se nao implicar
 	 */
	public boolean implies(Role impliedRole){
		if((WILDCARD_TOKEN.equals(resource) || impliedRole.getResource().equals(resource))){
			if((WILDCARD_TOKEN.equals(instance) || impliedRole.getInstance().equals(instance))){
				
				if(action.contains(WILDCARD_TOKEN)){
					return true;
				}else{
					String[] actionsImpliedRole = impliedRole.getAction().split(SUBPART_DIVIDER_TOKEN);
					String[] actionsGrantedRole = action.split(SUBPART_DIVIDER_TOKEN);
					
					for(int i=0;i<actionsImpliedRole.length;i++){
						boolean isActionGranted = false;
						
						if(ANYTHING_TOKEN.equals(actionsImpliedRole[i])){
							isActionGranted = true;
						}else{
							for(int j=0;j<actionsGrantedRole.length;j++){
								if(actionsImpliedRole[i].equals(actionsGrantedRole[j])){
									isActionGranted = true;
									break;
								}
							}
						}
						
						if(!isActionGranted){
							return false;
						}
					}
					
					return true;
				}
			}
		}
		
		return false;
	}

}


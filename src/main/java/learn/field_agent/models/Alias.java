package learn.field_agent.models;

public class Alias {

    private int aliasId;
    private String name;
    private String persona;


    private Agent agent;


    public Alias(int aliasId, String name, String persona) {
        this.aliasId = aliasId;
        this.name = name;
        this.persona = persona;
    }

    public Alias() {
    }

    public int getAliasId() {
        return aliasId;
    }

    public void setAliasId(int aliasId) {
        this.aliasId = aliasId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}

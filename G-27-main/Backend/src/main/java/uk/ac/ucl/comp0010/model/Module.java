package uk.ac.ucl.comp0010.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents the Module that students can learn.
 * The Module class contains information about the module code, name and mnc.
 */
@Entity
@Table(name = "Module")
public class Module {
  @Id
  @Column(name = "Code", nullable = false, unique = true)
  private String code;
  @Column(name = "Name", nullable = false)
  private String name;
  @Column(name = "MNC", nullable = false)
  private Boolean mnc;
  
  /**
   * Default constructor required by JPA.
   */
  public Module() {}
  
  /**
   * Creates a new Module object with the specified details.
   *
   * @param code which is the module's code
   * @param name which is the module name
   * @param mnc which is mandatory non-condonable
   */
  public Module(String code, String name, Boolean mnc) {
    this.code = code;
    this.name = name;
    this.mnc = mnc;
  }

  /**
   * Returns the code of this module.
   *
   * @return the code as a string
   */
  public String getCode() {
    return this.code;
  }

  /**
   * Returns the name of this module.
   *
   * @return the name as a string
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the mnc of this module.
   *
   * @return the mnc as a boolean
   */
  public Boolean getMnc() {
    return this.mnc;
  }

  /**
   * A setter method for the module's code.
   *
   * @param code which is the new code of the module
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * A setter method for the module's name.
   *
   * @param name which is the new name of the module
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * A setter method for the module's mnc.
   *
   * @param mnc which is the new mnc of the module
   */
  public void setMnc(Boolean mnc) {
    this.mnc = mnc;
  }

}
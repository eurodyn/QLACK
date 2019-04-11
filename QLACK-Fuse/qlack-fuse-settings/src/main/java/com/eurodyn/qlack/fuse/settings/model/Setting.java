package com.eurodyn.qlack.fuse.settings.model;

import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * This entity is used to store the internal settings of the application
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "set_setting")
@Getter
@Setter
public class Setting implements java.io.Serializable {

    /**
     * the unique id of the setting
     */
    @Id
    private String id;

    /**
     * the database version of the setting
     */
    @Version
    private long dbversion;

    /**
     * the owner of the setting
     */
    private String owner;

    /**
     * the name of the group, that this setting is part of
     */
    @Column(name = "group_name")
    private String group;

    /**
     * the key of the setting
     */
    @Column(name = "key_name")
    private String key;

    /**
     * the value of the setting
     */
    private String val;

    /**
     * the flag to define if setting is sensitive
     */
    @Column(name = "sensitivity")
    private boolean sensitive;

    /**
     * the flag to define if setting is a password
     */
    @Column(name = "psswrd")
    private Boolean password;

    /**
     * the date that the setting was created
     */
    @Column(name = "created_on")
    private long createdOn;

    public Setting() {
        this.id = UUID.randomUUID().toString();
        this.createdOn = Instant.now().toEpochMilli();
    }

    @Override
    public String toString() {
        return "Setting [id=" + id + ", dbversion=" + dbversion + ", owner=" + owner + ", group="
            + group + ", key="
            + key + ", val=" + val + ", createdOn=" + createdOn + "]";
    }

}

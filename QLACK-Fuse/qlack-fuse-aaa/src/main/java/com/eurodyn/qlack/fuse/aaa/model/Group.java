package com.eurodyn.qlack.fuse.aaa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The persistent class for the aaa_group database table.
 */
@Entity
@Table(name = "aaa_group")
@Getter
@Setter
public class Group extends AAAModel {

    private static final long serialVersionUID = 1L;

    @Version
    private long dbversion;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

    @Column(name = "object_id")
    private String objectId;

    @ManyToOne
    @JoinColumn(name = "parent")
    private Group parent;

    @OneToMany(mappedBy = "parent")
    private List<Group> children;

    /**
     * bi-directional many-to-one association to GroupHasOperation
     */
    @OneToMany(mappedBy = "group")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<GroupHasOperation> groupHasOperations;

    /**
     * bi-directional many-to-many association to Group
     */
    @ManyToMany
    @JoinTable(name = "aaa_user_has_group",
        joinColumns = {@JoinColumn(name = "group_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> users;

    public Group() {
        setId(UUID.randomUUID().toString());
    }

    public GroupHasOperation addGroupHasOperation(GroupHasOperation groupHasOperation) {
        if (getGroupHasOperations() == null) {
            setGroupHasOperations(new ArrayList<>());
        }
        getGroupHasOperations().add(groupHasOperation);
        groupHasOperation.setGroup(this);

        return groupHasOperation;
    }

    public GroupHasOperation removeGroupHasOperation(GroupHasOperation groupHasOperation) {
        getGroupHasOperations().remove(groupHasOperation);
        groupHasOperation.setGroup(null);

        return groupHasOperation;
    }

}
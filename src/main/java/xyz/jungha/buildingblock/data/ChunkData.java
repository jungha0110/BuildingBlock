package xyz.jungha.buildingblock.data;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChunkData {
    private UUID ownerUuid;
    private final List<UUID> memberUuids;

    public ChunkData(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
        this.memberUuids = new ArrayList<>();
    }

    public ChunkData(UUID ownerUuid, List<UUID> memberUuids) {
        this.ownerUuid = ownerUuid;
        this.memberUuids = new ArrayList<>(memberUuids);
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public List<UUID> getMemberUuids() {
        return new ArrayList<>(memberUuids);
    }

    public void addMember(UUID memberUuid) {
        if (!memberUuids.contains(memberUuid)) {
            memberUuids.add(memberUuid);
        }
    }

    public void removeMember(UUID memberUuid) {
        memberUuids.remove(memberUuid);
    }

    public boolean isMember(UUID memberUuid) {
        return memberUuids.contains(memberUuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkData chunkData = (ChunkData) o;
        return Objects.equals(ownerUuid, chunkData.ownerUuid) && Objects.equals(memberUuids, chunkData.memberUuids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerUuid, memberUuids);
    }
}
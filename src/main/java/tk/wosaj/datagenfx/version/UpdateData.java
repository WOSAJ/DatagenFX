package tk.wosaj.datagenfx.version;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class UpdateData {
    private String version;
    private String status;
    private String date;
    private List<String> changelog;

    public UpdateData(String version, String status, String date, List<String> changelog) {
        this.version = version;
        this.status = status;
        this.date = date;
        this.changelog = changelog;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UpdateData) obj;
        return Objects.equals(this.version, that.version) &&
                Objects.equals(this.status, that.status) &&
                Objects.equals(this.date, that.date) &&
                Objects.equals(this.changelog, that.changelog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, status, date, changelog);
    }

    @Override
    public String toString() {
        return "UpdateData[" +
                "version=" + version + ", " +
                "status=" + status + ", " +
                "date=" + date + ", " +
                "changelog=" + changelog + ']';
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getChangelog() {
        return changelog;
    }

    public void setChangelog(List<String> changelog) {
        this.changelog = changelog;
    }
}

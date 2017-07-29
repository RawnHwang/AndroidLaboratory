package me.hwang.picprocessing.bean;

public class PictureFolder {
    private String folderName;
    private String coverImagePath;
    private int pictureNum;
    private boolean isChecked;

    public PictureFolder(String folderName, String coverImagePath, int pictureNum, boolean isChecked) {
        this.folderName = folderName;
        this.coverImagePath = coverImagePath;
        this.pictureNum = pictureNum;
        this.isChecked = isChecked;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public int getPictureNum() {
        return pictureNum;
    }

    public void setPictureNum(int pictureNum) {
        this.pictureNum = pictureNum;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PictureFolder folder = (PictureFolder) o;

        if (pictureNum != folder.pictureNum) return false;
        if (!folderName.equals(folder.folderName)) return false;
        return coverImagePath.equals(folder.coverImagePath);
    }

    @Override
    public int hashCode() {
        int result = folderName.hashCode();
        result = 31 * result + coverImagePath.hashCode();
        result = 31 * result + pictureNum;
        return result;
    }
}

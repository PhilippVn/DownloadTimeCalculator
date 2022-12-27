package downloadtimecalculator;

public class Calculator {
    private SizeUnit sizeUnit;
    private SpeedUnit speedUnit;
    private double fileSize;
    private double downloadSpeed;

    public Calculator(SizeUnit sizeUnit, SpeedUnit speedUnit, double fileSize, double downloadSpeed) {
        this.sizeUnit = sizeUnit;
        this.speedUnit = speedUnit;
        this.fileSize = fileSize;
        this.downloadSpeed = downloadSpeed;
    }

    /*
    Kilobytes to bytes: bytes = Kilobytes * (10^3)
    Kibibytes to bytes: bytes = Kibibytes * (2^10)
    Megabytes to bytes: bytes = Megabytes * (10^6)
    Mebibytes to bytes: bytes = Mebibytes * (2^20)
    Gigabytes to bytes: bytes = Gigabytes * (10^9)
    Gibibytes to bytes: bytes = Gibibytes * (2^30)
    */

    /**
     * calculate the remaining download time based on given downloadspeed and filesize
     *
     * @return remaining downloadtime in seconds (not rounded)
     */
    public double calculateRemainingDownloadSeconds(){
        double fileSizeBytes = switch (sizeUnit){
            case GIGA_BYTE -> fileSize * Math.pow(10, 9);
            case GIBI_BYTE -> fileSize * Math.pow(2, 30);
            case MEGA_BYTE -> fileSize * Math.pow(10, 6);
            case MEBI_BYTE -> fileSize * Math.pow(2, 20);
            case KILO_BYTE -> fileSize * Math.pow(10, 3);
            case KIBI_BYTE -> fileSize * Math.pow(2, 10);
            case BYTE -> fileSize;
        };

        double downloadSpeedBytesPerSecond = switch(speedUnit){
            case GIGABYTES_PER_SECOND -> downloadSpeed * Math.pow(10, 9);
            case GIBIBYTES_PER_SECOND -> downloadSpeed * Math.pow(2, 30);
            case MEGABYTES_PER_SECOND -> downloadSpeed * Math.pow(10, 6);
            case MEBIBYTES_PER_SECOND -> downloadSpeed * Math.pow(2, 20);
            case KILOBYTES_PER_SECOND -> downloadSpeed * Math.pow(10, 3);
            case KIBIBYTES_PER_SECOND -> downloadSpeed * Math.pow(2, 10);
            case BYTES_PER_SECOND -> downloadSpeed;
        };

        // calculate seconds left:
        return fileSizeBytes / downloadSpeedBytesPerSecond;
    }

    /**
     * returns the remaining download time
     *
     * rounds seconds to next bigger number
     *
     * @param modulo if true it will modulo the seconds to days,hours,minutes,seconds otherwise it will just return seconds
     * @return pretty printed remaining download time
     */
    public String getRemainingDownloadTime(boolean modulo){
        int remainingDownloadTime = (int) Math.ceil(calculateRemainingDownloadSeconds());
        if(!modulo){
            return   remainingDownloadTime + "s";
        }else{
            int days = remainingDownloadTime / 86400; // a day has 60^2 * 24 seconds
            remainingDownloadTime %= 86400;
            int hours = remainingDownloadTime / 3600;
            remainingDownloadTime %= 3600;
            int minutes = remainingDownloadTime / 60;
            remainingDownloadTime %= 60;
            int seconds = remainingDownloadTime;

            if(days > 0){
                return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
            }

            if(hours > 0){
                return hours + "h " + minutes + "m " + seconds + "s";
            }

            if(minutes > 0){
                return minutes + "m " + seconds + "s";
            }

            return remainingDownloadTime + "s";
        }
    }
}

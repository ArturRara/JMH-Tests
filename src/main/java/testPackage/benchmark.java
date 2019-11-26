package testPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

import net.jpountz.lz4.LZ4BlockOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;

import com.github.luben.zstd.ZstdOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;


@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@Threads(1)
@Fork(1)
@Warmup(iterations = 2)
public class benchmark {

    byte[] buffer = new byte[1024];
    FileInputStream input;
    FileOutputStream output;

    public void prepareFiles(File source, File destination) throws IOException {
        input = new FileInputStream(source);
        output = new FileOutputStream(destination);
    }

    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        @Param({"photo1.jpg", "smallFile.bin","bigFile.bin","sampleVideo.mp4"})
        public String name;
        private Map<String, File> DATA_FOR_TESTING = new TreeMap<>();


        @Setup
        public void setup() {
            DATA_FOR_TESTING.put("inputFile", new File("kompresowane/" + name));
            DATA_FOR_TESTING.put("outputFile", new File("skompresowane/" + name));
        }
    }

    @TearDown
    public void tearDown() throws IOException {
        output.close();
        input.close();
    }

    @Benchmark
    public void gzip(ExecutionPlan plan, Blackhole bh) {

        try {
            Map<String, File> DATA_FOR_TESTING = plan.DATA_FOR_TESTING;
            prepareFiles(DATA_FOR_TESTING.get("inputFile"), DATA_FOR_TESTING.get("outputFile"));
            GZIPOutputStream gzip = new GZIPOutputStream(output);
            int read;
            while ((read = input.read(buffer)) != -1) {
                gzip.write(buffer, 0, read);
            }
            bh.consume(gzip);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void zstd(ExecutionPlan plan, Blackhole bh) throws IOException {
        try {
            Map<String, File> DATA_FOR_TESTING = plan.DATA_FOR_TESTING;
            prepareFiles(DATA_FOR_TESTING.get("inputFile"), DATA_FOR_TESTING.get("outputFile"));
            ZstdOutputStream zstd = new ZstdOutputStream(output);
            int read;
            while ((read = input.read(buffer)) != -1) {
                zstd.write(buffer, 0, read);
            }
            bh.consume(zstd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void xz(ExecutionPlan plan, Blackhole bh) throws IOException {
        try {
            Map<String, File> DATA_FOR_TESTING = plan.DATA_FOR_TESTING;
            prepareFiles(DATA_FOR_TESTING.get("inputFile"), DATA_FOR_TESTING.get("outputFile"));
            XZCompressorOutputStream xz = new XZCompressorOutputStream(output);
            int read;
            while ((read = input.read(buffer)) != -1) {
                xz.write(buffer, 0, read);
            }
            bh.consume(xz);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void lz4(ExecutionPlan plan, Blackhole bh) throws IOException {
        try {
            Map<String, File> DATA_FOR_TESTING = plan.DATA_FOR_TESTING;
            prepareFiles(DATA_FOR_TESTING.get("inputFile"), DATA_FOR_TESTING.get("outputFile"));
            LZ4BlockOutputStream lz4 = new LZ4BlockOutputStream(output);
            int read;
            while ((read = input.read(buffer)) != -1) {
                lz4.write(buffer, 0, read);
            }
            bh.consume(lz4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void pack200(ExecutionPlan plan, Blackhole bh) throws IOException {
        try {
            Map<String, File> DATA_FOR_TESTING = plan.DATA_FOR_TESTING;
            prepareFiles(DATA_FOR_TESTING.get("inputFile"), DATA_FOR_TESTING.get("outputFile"));
            Pack200CompressorOutputStream pack200 = new Pack200CompressorOutputStream(output);
            int read;
            while ((read = input.read(buffer)) != -1) {
                pack200.write(buffer, 0, read);
            }
            bh.consume(pack200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void lzma(ExecutionPlan plan, Blackhole bh) throws IOException {
        try {
            Map<String, File> DATA_FOR_TESTING = plan.DATA_FOR_TESTING;
            prepareFiles(DATA_FOR_TESTING.get("inputFile"), DATA_FOR_TESTING.get("outputFile"));
            LZMACompressorOutputStream lzma = new LZMACompressorOutputStream(output);
            int read;
            while ((read = input.read(buffer)) != -1) {
                lzma.write(buffer, 0, read);
            }
            bh.consume(lzma);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void deflate(ExecutionPlan plan, Blackhole bh) throws IOException {
        try {
            Map<String, File> DATA_FOR_TESTING = plan.DATA_FOR_TESTING;
            prepareFiles(DATA_FOR_TESTING.get("inputFile"), DATA_FOR_TESTING.get("outputFile"));
            DeflateCompressorOutputStream deflate = new DeflateCompressorOutputStream(output);

            int read;
            while ((read = input.read(buffer)) != -1) {
                deflate.write(buffer, 0, read);
            }
            bh.consume(deflate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

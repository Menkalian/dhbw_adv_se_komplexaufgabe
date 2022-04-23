package dhbw.ase.app2.pso;

public class PsoParameters {
    private final long maxIterations;

    private final int particleCount;

    private final double inertia;
    private final double socialRatio;
    private final double cognitiveRatio;

    public PsoParameters(long maxIterations, int particleCount, double inertia, double socialRatio, double cognitiveRatio) {
        this.maxIterations = maxIterations;
        this.particleCount = particleCount;
        this.inertia = inertia;
        this.socialRatio = socialRatio;
        this.cognitiveRatio = cognitiveRatio;
    }

    public long getMaxIterations() {
        return maxIterations;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public double getInertia() {
        return inertia;
    }

    public double getSocialRatio() {
        return socialRatio;
    }

    public double getCognitiveRatio() {
        return cognitiveRatio;
    }
}

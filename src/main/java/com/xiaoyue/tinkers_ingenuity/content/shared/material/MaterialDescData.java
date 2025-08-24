package com.xiaoyue.tinkers_ingenuity.content.shared.material;

import com.tterrag.registrate.providers.RegistrateLangProvider;

import javax.annotation.Nullable;

public record MaterialDescData(String id, @Nullable String flavor, @Nullable String encyclopedia, @Nullable String ranged, @Nullable String armor) {
    public String name() {
        return RegistrateLangProvider.toEnglishName(this.id);
    }

    public String langKey() {
        return "material.tinkers_ingenuity." + this.id;
    }

    public String flavorKey() {
        return this.langKey() + ".flavor";
    }

    public String encyclopediaKey() {
        return this.langKey() + ".encyclopedia";
    }

    public String rangedKey() {
        return this.langKey() + ".ranged";
    }

    public String armorKey() {
        return this.langKey() + ".armor";
    }

    public void caseAddLangData(RegistrateLangProvider pvd) {
        if (this.flavor() != null) {
            pvd.add(this.flavorKey(), this.flavor());
        }

        if (this.encyclopedia() != null) {
            pvd.add(this.encyclopediaKey(), this.encyclopedia());
        }

        if (this.ranged() != null) {
            pvd.add(this.rangedKey(), this.ranged());
        }

        if (this.armor() != null) {
            pvd.add(this.armorKey(), this.armor());
        }

    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static class Builder {
        public final String id;
        private String flavor;
        private String encyclopedia;
        private String ranged;
        private String armor = null;

        private Builder(String id) {
            this.id = id;
        }

        public Builder flavor(String flavor) {
            this.flavor = flavor;
            return this;
        }

        public Builder encyclopedia(String encyclopedia) {
            this.encyclopedia = encyclopedia;
            return this;
        }

        public Builder ranged(String ranged) {
            this.ranged = ranged;
            return this;
        }

        public Builder armor(String armor) {
            this.armor = armor;
            return this;
        }

        public MaterialDescData build() {
            return new MaterialDescData(this.id, this.flavor, this.encyclopedia, this.ranged, this.armor);
        }
    }
}

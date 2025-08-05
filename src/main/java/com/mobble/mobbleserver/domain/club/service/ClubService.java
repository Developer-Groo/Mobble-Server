    private final ClubRepository clubRepository;
    private final ClubCategoryRepository clubCategoryRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubAgeGroupRepository clubAgeGroupRepository;
    private final ClubValidator clubValidator;
    private final MemberValidator memberValidator;
    @Transactional
    public void createClub(Long memberId, ClubRequestDto dto) {
        ClubCategory category = findCategoryOrThrow(dto.category());
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);

        Club club = dto.toEntity(category);
        clubRepository.save(club);

        ClubMember clubMember = ClubMember.createClubMember(member, club, ClubMemberRole.LEADER, true);
        clubMemberRepository.save(clubMember);

        List<ClubAgeGroup> ageGroups = createClubAgeGroups(club, dto.ageGroup());
        clubAgeGroupRepository.saveAll(ageGroups);
    }
    private ClubCategory findCategoryOrThrow(String categoryName) {
        return clubCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new DomainException(ClubErrorCode.CATEGORY_NOT_FOUND));
    }
    private List<ClubAgeGroup> createClubAgeGroups(Club club, List<ClubAgeGroupType> ageGroupTypes) {
        return ageGroupTypes.stream()
                .map(age -> ClubAgeGroup.createClubAgeGroup(club, age))
                .toList();
    }
